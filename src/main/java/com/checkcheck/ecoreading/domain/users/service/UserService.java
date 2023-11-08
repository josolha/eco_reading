package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.dto.UserLoginRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO.TokenInfo;
import com.checkcheck.ecoreading.domain.users.entity.Role;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.exception.AuthenticationEmailException;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import com.checkcheck.ecoreading.security.jwt.JwtTokenProvider;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode :";

    private final MailService mailService;

    private final RedisService redisService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public Long save(UserRegisterRequestDTO dto) {
        Users user = Users.builder()
                .email(dto.getEmail())
                .birthDate(dto.getBirthdate())
                .nickName(dto.getNickname())
                .phone(dto.getPhone())
                .userName(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Role.ROLE_USER)
                .detailAddress(dto.getDetailAddress())
                .roadAddress(dto.getRoadAddress())
                .postcode(dto.getPostcode())
                .enabled(true)
                .emailVerified(false)
                .build();
        user = userRepository.save(user);
        return user.getUsersId();
    }

    @Transactional
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "[췍췍 이메일 인증 번호]";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "Email" / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX+toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Users> member = userRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("userservice exception occur email: {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다.");
        }
    }

    private String createCode()  {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            //log.debug("MemberService.createCode() exception occur");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "알고리즘 생성 에러");
        }
    }
    public void verifiedCode(String email, String code) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);

        if (redisAuthCode == null || !redisAuthCode.equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증 코드가 틀렸습니다 다시 확인부탁드립니다.");
        }
    }

    public TokenInfo login(UserLoginRequestDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication;
        try {
            // 인증 시도
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        } catch (BadCredentialsException e) {
//            // 사용자가 입력한 비밀번호가 올바르지 않을 때의 처리
//            throw new AuthenticationServiceException("비밀번호가 틀립니다.");
//        } catch (AuthenticationEmailException e) {
//            // 사용자 이름을 찾을 수 없을 때의 처리
//            throw new AuthenticationServiceException("해당 사용자를 찾을 수 없습니다.");
//        } catch (AuthenticationException e) {
//            // 기타 인증 관련 예외 처리
//            throw new AuthenticationServiceException("인증 과정에서 문제가 발생했습니다: ");
//        }
        }catch (Exception e){
            throw new AuthenticationServiceException("이메일 또는 비밀번호가 틀렸습니다.");
        }
        // 인증 성공, JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        log.info("tokenInfo = {}", tokenInfo);
        return tokenInfo;
    }
}



