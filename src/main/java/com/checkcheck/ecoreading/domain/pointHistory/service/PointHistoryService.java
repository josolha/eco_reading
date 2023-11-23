package com.checkcheck.ecoreading.domain.pointHistory.service;

import com.checkcheck.ecoreading.domain.pointHistory.dto.PointHistoryDTO;
import com.checkcheck.ecoreading.domain.pointHistory.entity.PointHistory;
import com.checkcheck.ecoreading.domain.pointHistory.repository.PointHistoryRepository;
import com.checkcheck.ecoreading.domain.pointHistory.entity.PointHistoryForm;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    // PointHistory 엔티티를 PointHistoryDTO로 변환 메서드
    public PointHistoryDTO convertToPointHistoryDTO(PointHistory pointHistory) {
        PointHistoryDTO pointHistoryDTO = new PointHistoryDTO();

        pointHistoryDTO.setPointHistoryId(pointHistory.getPointHistoryId());
        pointHistoryDTO.setUsers(pointHistory.getUsers());
        pointHistoryDTO.setTransactions(pointHistory.getTransactions());
        pointHistoryDTO.setForm(pointHistory.getForm());
        pointHistoryDTO.setPoint(pointHistory.getPoint());

        return pointHistoryDTO;
    }

    // PointHistory 테이블에 값 insert 메서드(나눔받기시 5 포인트 차감)
    public PointHistory insertPointHistory(Long id, Transactions transactions) {
        Users user = userRepository.findAllByUsersId(id);

        PointHistory pointHistory = PointHistory.builder()
                .users(user)
                .transactions(transactions)
                .form(PointHistoryForm.MINUS)
                .point(5)
                .build();

        pointHistoryRepository.save(pointHistory);
        return pointHistory;
    }
}
