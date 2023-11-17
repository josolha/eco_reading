package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.dto.UserOAuth2CustomDTO;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.exception.AuthenticationEmailException;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserOAuthCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // 사용자 정보를 조회하는 데 사용하는 JPA 레포지토리

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        UserOAuth2CustomDTO customUser = new UserOAuth2CustomDTO(oAuth2User);
        // 사용자의 새로운 회원 여부를 확인하고 설정
        boolean isNew = isNewUser(customUser.getEmail(), customUser.getSocialId());
        customUser.setNewUser(isNew);
        return customUser;
    }

    private boolean isNewUser(String email, Long socialAuthId) {
        Optional<Users> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            Users user = existingUser.get();

            // 카카오 계정으로 이미 가입된 사용자인 경우
            if (user.getSocialAuthId() != null && user.getSocialAuthId().equals(socialAuthId)) {
                // 이미 가입된 카카오 계정으로 로그인하는 경우 정상 처리
                return false;
            } else if (user.getSocialAuthId() == null || !user.getSocialAuthId().equals(socialAuthId)) {
                // 다른 방식으로 가입된 사용자 또는 다른 카카오 계정 사용자의 경우 예외 발생
                throw new AuthenticationEmailException("이미 존재하는 이메일 주소입니다.");
            }
        }
        return true; // 새로운 사용자
    }
}
