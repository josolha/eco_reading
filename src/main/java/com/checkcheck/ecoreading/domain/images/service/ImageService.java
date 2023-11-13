package com.checkcheck.ecoreading.domain.images.service;

import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.images.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    @Transactional
    public void deleteImage(Long imageId){
        Images images = imageRepository.findByImagesId(imageId);
        imageRepository.delete(images);
    }
}
