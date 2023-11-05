package com.checkcheck.ecoreading.domain.users.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserRegisterRequestDTO {
    private String email;
    private String password;
}
