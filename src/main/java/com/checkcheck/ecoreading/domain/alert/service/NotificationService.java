package com.checkcheck.ecoreading.domain.alert.service;

import com.checkcheck.ecoreading.domain.alert.dto.AlertSendDTO;
import com.checkcheck.ecoreading.domain.alert.entity.Alert;
import com.checkcheck.ecoreading.domain.alert.repository.AlertRepository;
import com.checkcheck.ecoreading.domain.alert.repository.EmitterRepository;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final UserRepository usersRepository;
    private final AlertRepository alertRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    public void sendNotification(Long userId, String message) {
        Optional<SseEmitter> emitter = emitterRepository.get(userId);
        // 로그인 중
        if (emitter.isPresent()) {
            sendRealtimeNotification(emitter.get(), userId, message); // emitter.get()만 전달
            // 로그아웃 상태
        } else {
            saveOfflineNotification(userId, message);
        }
    }

    private void sendRealtimeNotification(SseEmitter emitter, Long userId, String message) {
        try {
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            // 새 Alert 객체 생성 및 저장
            Alert newAlert = Alert.builder()
                    .user(user)
                    .message(message)
                    .build();
            alertRepository.save(newAlert);

            // 새 알림만 DTO로 변환 후 JSON으로 변환
            AlertSendDTO newAlertDto = AlertSendDTO.fromEntity(newAlert);
            String notificationJson = new Gson().toJson(newAlertDto);

            // 클라이언트에 새 알림만 전송
            emitter.send(SseEmitter.event().name("new-alert").data(notificationJson));
        } catch (IOException exception) {
            emitterRepository.removeEmitter(userId);
            emitter.completeWithError(exception);
        }
    }
    //json 데이터 변환
    private String convertNotificationsListToJson(List<AlertSendDTO> alerts) {
        Gson gson = new Gson();
        return gson.toJson(alerts);
    }

    private void saveOfflineNotification(Long userId, String message) {
        // 먼저, Users 엔티티를 조회
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        // Alert 객체를 생성하고, Users 엔티티를 설정
        Alert alert = Alert.builder()
                .user(user) // Users 엔티티를 Alert 객체에 설정
                .message(message)
                .build();
        alertRepository.save(alert);
    }
    public SseEmitter init(Long userId) {
        System.out.println("userId =================== " + userId);
        // 기존 연결 확인
        Optional<SseEmitter> existingEmitter = emitterRepository.get(userId);
        System.out.println("existingEmitter = " + existingEmitter);
        if (existingEmitter.isPresent()) {
            existingEmitter.get().complete();
            emitterRepository.removeEmitter(userId);
        }
        // 새 연결 생성
        SseEmitter newEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.addEmitter(userId, newEmitter);
        // 새 연결에 대한 이벤트 핸들러 설정
        newEmitter.onCompletion(() -> emitterRepository.removeEmitter(userId));
        newEmitter.onTimeout(() -> emitterRepository.removeEmitter(userId));
        newEmitter.onError(e -> emitterRepository.removeEmitter(userId));

        // 사용자의 읽지 않은 알림 목록을 조회 및 전송
        sendUnreadNotifications(newEmitter, userId);
        return newEmitter;
    }
    private void sendUnreadNotifications(SseEmitter emitter, Long userId) {
        List<Alert> unreadAlerts = alertRepository.findByUser_UsersId(userId);
        List<AlertSendDTO> unreadAlertsDto = unreadAlerts.stream()
                .map(AlertSendDTO::fromEntity)
                .collect(Collectors.toList());

        if (!unreadAlerts.isEmpty()) {
            String notificationsJson = convertNotificationsListToJson(unreadAlertsDto);
            try {
                emitter.send(SseEmitter.event().name("init").data(notificationsJson));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    public void deleteNotification(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + alertId));
        alertRepository.delete(alert);
    }


}
