package com.checkcheck.ecoreading.domain.delivery.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.dto.UpdateDeliveryDTO;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "delivery")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    private int postcode;
    private String roadAddress;
    private String detailAddress;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private DeliveryPlace place; // 수거/배송 장소(문앞, 경비실, 무인택배함)

    @Enumerated(EnumType.STRING)
    private DeliveryForm form; // 배송형태 (수거 or 배송) 둘중 하나 무조건 선택.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "boards_id")
    private Boards boards;

    @ManyToOne
    @JoinColumn(name = "transactions_id")
    private Transactions transactions;

    public void changeDelivery(UpdateDeliveryDTO dto){
        this.detailAddress = dto.getDetailAddress();
        this.postcode = dto.getPostcode();
        this.name = dto.getName();
        this.form = dto.getForm();
        this.place = dto.getPlace();
        this.phone = dto.getPhone();
        this.roadAddress = dto.getRoadAddress();
    }

}
