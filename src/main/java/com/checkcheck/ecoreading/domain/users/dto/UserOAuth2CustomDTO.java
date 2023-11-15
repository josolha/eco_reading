package com.checkcheck.ecoreading.domain.users.dto;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserOAuth2CustomDTO implements OAuth2User {

    private OAuth2User oauth2User;
    private boolean isNewUser; // 새로운 회원 여부를 나타내는 필드

    public UserOAuth2CustomDTO(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getName();
    }

    public String getEmail() {
        Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
    }
    public String getNickname() {
        Map<String, Object> properties = oauth2User.getAttribute("properties");
        return properties != null ? (String) properties.get("nickname") : null;
    }
    // 새로운 회원 여부에 대한 getter와 setter
    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }

    @Override
    public String toString() {
        return "UserOAuth2CustomDTO{" +
                "oauth2User=" + oauth2User +
                ", isNewUser=" + isNewUser +
                '}';
    }

    public Long getSocialId() {
        return (Long) oauth2User.getAttribute("id");
    }

}
