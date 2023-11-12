package com.checkcheck.ecoreading.domain.pointHistory.repository;

import com.checkcheck.ecoreading.domain.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

}
