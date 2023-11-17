package com.checkcheck.ecoreading.domain.delivery.service;

import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.delivery.dto.DeliveryDTO;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.entity.DeliveryForm;
import com.checkcheck.ecoreading.domain.delivery.repository.DeliveryRepository;
import com.checkcheck.ecoreading.domain.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final TransactionService transactionService;
    private final BookService bookService;

    // Delivery 엔티티를 DeliveryDTO로 변환 메서드
    public DeliveryDTO convertToDeliveryDTO(Delivery delivery) {
        DeliveryDTO deliveryDTO = new DeliveryDTO();

        deliveryDTO.setDeliveryId(delivery.getDeliveryId());
        deliveryDTO.setTransactions(delivery.getTransactions());
        deliveryDTO.setBoards(delivery.getBoards());
        deliveryDTO.setPostcode(delivery.getPostcode());
        deliveryDTO.setRoadAddress(delivery.getRoadAddress());
        deliveryDTO.setDetailAddress(delivery.getDetailAddress());
        deliveryDTO.setForm(delivery.getForm());
        deliveryDTO.setName(delivery.getName());
        deliveryDTO.setPhone(deliveryDTO.getPhone());

        return deliveryDTO;
    }

    // Delivery 테이블에 값 insert 메서드
    public Delivery insertDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = Delivery.builder()
                .transactions(deliveryDTO.getTransactions())
                .boards(deliveryDTO.getBoards())
                .postcode(deliveryDTO.getPostcode())
                .roadAddress(deliveryDTO.getRoadAddress())
                .detailAddress(deliveryDTO.getDetailAddress())
                .form(DeliveryForm.DROP)
                .name(deliveryDTO.getName())
                .phone(deliveryDTO.getPhone())
                .build();

        deliveryRepository.save(delivery);

        return delivery;
    }

}
