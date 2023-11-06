package com.checkcheck.ecoreading.domain.images.repository;

import com.checkcheck.ecoreading.domain.images.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images, Long> {

}
