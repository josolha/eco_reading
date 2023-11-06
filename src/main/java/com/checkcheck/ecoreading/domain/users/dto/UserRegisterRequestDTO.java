package com.checkcheck.ecoreading.domain.users.dto;


import com.checkcheck.ecoreading.domain.users.entity.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class UserRegisterRequestDTO {
    private String email;
    private String emailcode;
    private String username;
    private String nickname;
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String phone;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
}
