package com.checkcheck.ecoreading.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserLoginRequestDTO {
    private String username;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
