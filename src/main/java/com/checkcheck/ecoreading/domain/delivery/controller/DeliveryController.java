package com.checkcheck.ecoreading.domain.delivery.controller;

import com.checkcheck.ecoreading.domain.delivery.dto.DeliveryDTO;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.delivery.repository.DeliveryRepository;
import com.checkcheck.ecoreading.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    // DB에 Delivery 데이터 insert 후 나눔받기 완료 출력
    @PostMapping("/board/detail/complete")
    public String completeTakeBook(@ModelAttribute DeliveryDTO deliveryDTO) {
        deliveryService.insertDelivery(deliveryDTO);

        return "/content/board/completeTakeBook";
    }

}
