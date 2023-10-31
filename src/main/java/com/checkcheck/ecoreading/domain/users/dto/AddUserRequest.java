package com.checkcheck.ecoreading.domain.users.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AddUserRequest {
    private String email;
    private String password;
}
