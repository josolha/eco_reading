package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.dto.UserKakaoRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserLoginRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserOAuth2CustomDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserRegisterRequestDTO;
import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO.TokenInfo;
import com.checkcheck.ecoreading.domain.users.entity.Role;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import com.checkcheck.ecoreading.security.jwt.JwtTokenProvider;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;

import java.util.List;

import java.util.Arrays;

import java.util.Optional;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode:";

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
    public Long saveKakao(UserKakaoRegisterRequestDTO dto){
        Users user = Users.builder()
                .email(dto.getEmail())
                .birthDate(dto.getBirthdate())
                .nickName(dto.getNickname())
                .phone(dto.getPhone())
                .userName(dto.getUsername())
                .socialAuthId(dto.getSocialAuthId())
                .socialAuth("kakao")
                .password(null)
                .role(Role.ROLE_USER)
                .detailAddress(dto.getDetailAddress())
                .roadAddress(dto.getRoadAddress())
                .postcode(dto.getPostcode())
                .enabled(true)
                .emailVerified(true)
                .build();
        user = userRepository.save(user);
        return user.getUsersId();
    }

    @Transactional
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "[eco-reading 이메일 인증 번호]";

       String authCode = this.createCode();

//        String authCode = "<p>eco-reading 인증 번호 입니다.<p>"
//                + "<p> 인증 번호 : " + this.createCode() + "<p>";

        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "Email" / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX+toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Users> member = userRepository.findByEmailAndSocialAuthIsNull(email);
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

    public TokenInfo kakaoLogin(UserOAuth2CustomDTO oauthUser, HttpServletResponse response) {
        // 사용자의 소셜 고유 ID를 기반으로 데이터베이스에서 사용자 조회
        Long socialAuthId = oauthUser.getSocialId();
        Optional<Users> userOpt = userRepository.findBySocialAuthId(socialAuthId);
        Users user;
        // 이미 존재하는 사용자
        user = userOpt.get();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        // 응답에 토큰 쿠키 추가
        addTokenCookiesToResponse(tokenInfo, response);
        log.info("Kakao user logged in: {}", user.getEmail());
        return tokenInfo;
    }

    public TokenInfo login(UserLoginRequestDTO loginDto, HttpServletResponse response) {
        Authentication authentication = authenticateUser(loginDto);
        // 바로 JwtTokenProvider를 사용하여 토큰을 생성합니다.
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        addTokenCookiesToResponse(tokenInfo, response);
        log.info("tokenInfo = {}", tokenInfo);
        return tokenInfo;
    }

    private Authentication authenticateUser(UserLoginRequestDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        try {
            return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new AuthenticationServiceException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }
    private void addTokenCookiesToResponse(TokenInfo tokenInfo, HttpServletResponse response) {
        Cookie accessTokenCookie = createCookie("accessToken", tokenInfo.getAccessToken(), 10L);
        Cookie refreshTokenCookie = createCookie("refreshToken", tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime());
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
    private Cookie createCookie(String name, String value, Long maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge.intValue());
        return cookie;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            deleteCookies(request, response, "accessToken", "refreshToken");
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String userEmail = userDetails.getUsername();
                redisService.deleteValues("RefreshToken:" + userEmail);
            } else if (authentication.getPrincipal() instanceof String) {
                String username = (String) authentication.getPrincipal();
                redisService.deleteValues("RefreshToken:" + username);
            } else {
                throw new IllegalStateException("Unexpected type of principal object");
            }
        }
    }
    private void deleteCookies(HttpServletRequest request, HttpServletResponse response, String... cookieNames) {
        Arrays.asList(cookieNames).forEach(cookieName -> {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            cookie.setPath("/"); // 쿠키 경로를 설정합니다. 일반적으로 루트("/")를 사용합니다.
            cookie.setMaxAge(0); // 쿠키를 즉시 만료시킵니다.
            response.addCookie(cookie);
        });
    }

    public Optional<Users> findByUserNameAndPhone(String name, String phone){
        return userRepository.findByUserNameAndPhone(name,phone);
    }

    public List<Users> findAll(){
        return userRepository.findAll();
    }

    public Users findAllById(Long usersId){
       return userRepository.findAllByUsersId(usersId);
    }



}



