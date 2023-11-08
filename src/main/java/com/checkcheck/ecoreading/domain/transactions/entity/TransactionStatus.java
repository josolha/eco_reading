package com.checkcheck.ecoreading.domain.transactions.entity;

public enum TransactionStatus {
    //거래 상태 (신규등록, 수거중, 검수중, 검수완료(나눔중), 나눔완료)
    NEW,
    PICKINGUP,
    CHECKING,
    SHARING,
    COMPLETE;
}
