package com.checkcheck.ecoreading.domain.delivery.dto;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryForm;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryDTO {
    private String bookTitle;
    private Long deliveryId;
    private Transactions transactions;
    private Boards boards;
    private int postcode;
    private String roadAddress;
    private String detailAddress;
    private DeliveryForm form;
    private String name;
    private String phone;
}
