package com.checkcheck.ecoreading.domain.alert.controller;

import com.checkcheck.ecoreading.domain.alert.service.NotificationService;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping(value = "/init", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe( HttpServletRequest request) {
        Long id = userService.getUserIdFromAccessTokenCookie(request);
        return notificationService.init(id);
    }
    @DeleteMapping("/delete/{alertId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long alertId) {
        try {
            notificationService.deleteNotification(alertId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting notification");
        }
    }
}
