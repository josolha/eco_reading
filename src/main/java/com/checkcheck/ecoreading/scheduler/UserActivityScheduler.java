package com.checkcheck.ecoreading.scheduler;


import com.checkcheck.ecoreading.domain.loginHistory.repository.LoginHistoryRepository;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActivityScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 1 * * MON") // 매주 월요일 오전 1시에 실행
    public void deactivateInactiveUsers() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        List<Users> inactiveUsers = userRepository.findUsersWithNoLoginSince(oneYearAgo);
        inactiveUsers.forEach(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }

}
