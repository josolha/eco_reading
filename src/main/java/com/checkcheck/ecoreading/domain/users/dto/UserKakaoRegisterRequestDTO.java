package com.checkcheck.ecoreading.domain.users.dto;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Setter
public class UserKakaoRegisterRequestDTO {

    private Long socialAuthId;
    private String email;
    private String nickname;
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String phone;

    private String postcode;

    private String roadAddress;

    private String detailAddress;

}
