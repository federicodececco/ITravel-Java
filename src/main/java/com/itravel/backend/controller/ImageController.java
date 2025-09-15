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

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/images")
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
    public ResponseEntity<Image> upload(@RequestParam("file") MultipartFile image,
            @RequestParam("travelId") String travelId, @RequestParam("PageId") String pageId,
            @RequestParam("isCover") boolean isCover) throws IOException {

        try {
            Optional<Travel> optTravel = travelService.findById(Long.valueOf(travelId));
            if (optTravel.isEmpty()) {

                return ResponseEntity.notFound().build();

            }
            Optional<Page> optPage = pageService.findById(Long.valueOf(pageId));
            if (optPage.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String url = cloudFlareR2Service.uploadFile(image);
            Image img = new Image();
            img.setImageUrl(url);
            img.setIsCover(isCover);
            img.setPage(optPage.get());
            img.setTravel(optTravel.get());
            return ResponseEntity.ok(imageService.save(img));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}