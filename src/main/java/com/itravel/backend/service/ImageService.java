package com.itravel.backend.service;

import com.itravel.backend.dto.UploadResult;
import com.itravel.backend.models.Image;
import com.itravel.backend.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image save(Image image) {
        if (image.getId() == null) {
            image.setId(UUID.randomUUID().toString());
        }
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());
        return imageRepository.save(image);
    }

    public List<Image> saveMultiple(List<UploadResult> uploadResults, Long travelId, Long pageId) {
        List<Image> images = new ArrayList<>();

        for (UploadResult result : uploadResults) {
            Image image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setImageUrl(result.getPublicUrl());
            image.setCreatedAt(LocalDateTime.now());
            image.setUpdatedAt(LocalDateTime.now());

            if (travelId != null) {

            }

            if (pageId != null) {

            }

            images.add(imageRepository.save(image));
        }

        return images;
    }

    public Image findById(String id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElse(null);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public List<Image> findByTravelId(Long travelId) {

        return new ArrayList<>();
    }

    public List<Image> findByPageId(Long pageId) {

        return new ArrayList<>();
    }

    public void deleteById(String id) {
        imageRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return imageRepository.existsById(id);
    }

    public long count() {
        return imageRepository.count();
    }
}
