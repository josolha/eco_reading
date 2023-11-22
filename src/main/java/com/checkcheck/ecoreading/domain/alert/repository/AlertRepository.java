package com.checkcheck.ecoreading.domain.alert.repository;

import com.checkcheck.ecoreading.domain.alert.entity.Alert;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    // 사용자 ID와 상태로 알림을 조회하는 메소드
    List<Alert> findByUser_UsersId(Long usersId);
}
