package com.itravel.backend.controller;

import com.itravel.backend.models.Image;
import com.itravel.backend.models.Page;
import com.itravel.backend.models.Travel;
import com.itravel.backend.service.CloudFlareR2Service;
import com.itravel.backend.service.ImageService;
import com.itravel.backend.service.PageService;
import com.itravel.backend.service.TravelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TravelService travelService;

    @Autowired
    private PageService pageService;

    @Autowired
    private CloudFlareR2Service cloudFlareR2Service;

    @PostMapping("/save")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile image,
            @RequestParam("travelId") String travelId, @RequestParam("pageId") String pageId,
            @RequestParam("isCover") boolean isCover) {

        try {

            if (image.isEmpty()) {
                return ResponseEntity.badRequest().body("File cannot be empty");
            }

            Optional<Travel> optTravel = travelService.findById(Long.valueOf(travelId));
            if (optTravel.isEmpty()) {
                return ResponseEntity.badRequest().body("Travel not found with id: " + travelId);
            }

            Optional<Page> optPage = pageService.findById(Long.valueOf(pageId));
            if (optPage.isEmpty()) {
                return ResponseEntity.badRequest().body("Page not found with id: " + pageId);
            }

            String imageKey = cloudFlareR2Service.uploadFile(image);

            Image img = new Image();
            img.setImageUrl(imageKey);
            img.setIsCover(isCover);
            img.setPage(optPage.get());
            img.setTravel(optTravel.get());

            Image savedImage = imageService.save(img);
            return ResponseEntity.ok(savedImage);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable String id) {
        try {
            Image image = imageService.findById(id);
            if (image == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving image");
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllImages() {
        try {
            return ResponseEntity.ok(imageService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving images");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable String id) {
        try {
            if (!imageService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            imageService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        }
    }
}