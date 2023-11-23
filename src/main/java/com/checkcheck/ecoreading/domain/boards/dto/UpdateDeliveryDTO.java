package com.checkcheck.ecoreading.domain.boards.dto;

import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryForm;
import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryPlace;
import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class UpdateDeliveryDTO {
    private int postcode;
    private String roadAddress;
    private String detailAddress;
    private DeliveryForm form;  // 배송형태 (수거 or 배송) 둘중 하나 무조건 선택.
    private DeliveryPlace place; // 수거/배송 장소(문앞, 경비실, 무인택배함)
    private String name; //이름
    private String phone; //휴대폰 번호
}

