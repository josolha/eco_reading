package com.checkcheck.ecoreading.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


public class UserResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @ToString
    public static class TokenInfo {
        private String accessToken;
        private Long accessTokenExpirationTime;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}
