package com.checkcheck.ecoreading.domain.users.dto;

public class EmailVerificationResultDTO {
    private boolean verified; // 인증 결과를 저장하는 변수

    // 생성자는 private으로 선언하여 외부에서 직접 인스턴스를 생성하지 못하도록 합니다.
    private EmailVerificationResultDTO(boolean verified) {
        this.verified = verified;
    }

    // 인증 결과에 따라 인스턴스를 생성하는 static factory method
    public static EmailVerificationResultDTO of(boolean verified) {
        return new EmailVerificationResultDTO(verified);
    }

    // 인증 결과를 가져오는 getter 메서드
    public boolean isVerified() {
        return verified;
    }
}
