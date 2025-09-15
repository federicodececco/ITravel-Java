package com.itravel.backend.service;

import com.itravel.backend.dto.UploadResult;
import com.itravel.backend.models.Image;
import com.itravel.backend.models.Travel;
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

    public List<Image> saveMultiple(List<UploadResult> uploadResults, Travel travel, Page page) {
        List<Image> images = new ArrayList<>();

        for (UploadResult result : uploadResults) {
            Image image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setImageUrl(result.getPublicUrl());
            image.setCreatedAt(LocalDateTime.now());
            image.setUpdatedAt(LocalDateTime.now());

            if (travel != null) {
                image.setTravel(travel);
            }

            if (page != null) {
                image.setPage(page);
            }

            images.add(imageRepository.save(image));
        }

        return images;
    }

    public Image findById(String id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElse(null);
    }

    public Optional<Image> findByIdOptional(String id) {
        return imageRepository.findById(id);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public List<Image> findByTravelId(Long travelId) {

        return imageRepository.findAll().stream()
                .filter(img -> img.getTravel() != null && img.getTravel().getId().equals(travelId)).toList();
    }

    public List<Image> findByPageId(Long pageId) {

        return imageRepository.findAll().stream()
                .filter(img -> img.getPage() != null && img.getPage().getId().equals(pageId)).toList();
    }

    public List<Image> findByTravelIdAndIsCover(Long travelId, boolean isCover) {
        return imageRepository.findAll().stream().filter(img -> img.getTravel() != null
                && img.getTravel().getId().equals(travelId) && img.getIsCover().equals(isCover)).toList();
    }

    public List<Image> findByPageIdAndIsCover(Long pageId, boolean isCover) {
        return imageRepository.findAll().stream().filter(img -> img.getPage() != null
                && img.getPage().getId().equals(pageId) && img.getIsCover().equals(isCover)).toList();
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

    public void deleteByTravelId(Long travelId) {
        List<Image> imagesToDelete = findByTravelId(travelId);
        imageRepository.deleteAll(imagesToDelete);
    }

    public void deleteByPageId(Long pageId) {
        List<Image> imagesToDelete = findByPageId(pageId);
        imageRepository.deleteAll(imagesToDelete);
    }
}
