package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.dto.UserOAuth2CustomDTO;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.util.Map;
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

//        System.out.println("customUser = " + customUser.getName());
//        System.out.println("email = " + customUser.getEmail());
//        System.out.println("nickname = " + customUser.getNickname());

        // 사용자의 새로운 회원 여부를 확인하고 설정
        boolean isNew = isNewUser(customUser.getEmail(), customUser.getSocialId());
        customUser.setNewUser(isNew);
        return customUser;
    }
    private boolean isNewUser(String email, Long socialAuthId) {
        return userRepository.findByEmailAndSocialAuthId(email, socialAuthId).isEmpty();
    }
}
