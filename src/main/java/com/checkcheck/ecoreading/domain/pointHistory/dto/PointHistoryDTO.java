package com.checkcheck.ecoreading.domain.pointHistory.dto;

import com.checkcheck.ecoreading.domain.pointHistory.entity.PointHistoryForm;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointHistoryDTO {
    private Long pointHistoryId;
    private Users users;
    private Transactions transactions;
    private PointHistoryForm form;
    private int point;
}
