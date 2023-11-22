package com.checkcheck.ecoreading.domain.alert.dto;

import com.checkcheck.ecoreading.domain.alert.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class AlertSendDTO {
    private Long alertId;
    private Long userId;
    private String status;
    private String message;

    public static AlertSendDTO fromEntity(Alert alert) {
        AlertSendDTO dto = new AlertSendDTO();
        dto.alertId = alert.getAlert_id();
        dto.userId = alert.getUser().getUsersId();
        dto.message = alert.getMessage();
        return dto;
    }
}
