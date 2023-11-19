package com.checkcheck.ecoreading.domain.loginHistory.repository;

import com.checkcheck.ecoreading.domain.loginHistory.entitiy.LoginHistory;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    Optional<LoginHistory> findTopByUserOrderByLoginTimeDesc(Users user);

}
