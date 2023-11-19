package com.checkcheck.ecoreading.domain.loginHistory.service;

import com.checkcheck.ecoreading.domain.loginHistory.entitiy.LoginHistory;
import com.checkcheck.ecoreading.domain.loginHistory.repository.LoginHistoryRepository;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final UserRepository repository;

    private final LoginHistoryRepository loginHistoryRepository;


    public void saveLoginHistory(Authentication authentication) {
        // Authentication 객체에서 UserDetails 추출
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        // 사용자 정보를 기반으로 LoginHistory 엔티티 생성 및 저장
        Users user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setLoginTime(LocalDateTime.now());
        loginHistoryRepository.save(loginHistory);
    }

    public void updateLogoutTime(Users user) {
        // 가장 최근의 로그인 기록을 가져와 로그아웃 시간을 업데이트
        LoginHistory lastLoginHistory = loginHistoryRepository.findTopByUserOrderByLoginTimeDesc(user)
                .orElseThrow(() -> new IllegalStateException("Login history not found for user: " + user.getEmail()));
        lastLoginHistory.setLogoutTime(LocalDateTime.now());
        loginHistoryRepository.save(lastLoginHistory);
    }
}
