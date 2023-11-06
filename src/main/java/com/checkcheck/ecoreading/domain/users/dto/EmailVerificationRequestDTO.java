package com.checkcheck.ecoreading.domain.users.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EmailVerificationRequestDTO {
    @Email
    private String email;
    @NotBlank
    private String code;
}
