package com.checkcheck.ecoreading.domain.delivery.repository;

import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByDeliveryId(Long id);

}
