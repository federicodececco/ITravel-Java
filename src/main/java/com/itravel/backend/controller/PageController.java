package com.itravel.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itravel.backend.models.Image;
import com.itravel.backend.models.Page;
import com.itravel.backend.models.Travel;
import com.itravel.backend.service.CloudFlareR2Service;
import com.itravel.backend.service.ImageService;
import com.itravel.backend.service.PageService;
import com.itravel.backend.service.TravelService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/pages")
public class PageController {

    @Autowired
    PageService pageService;

    @Autowired
    TravelService travelService;

    @Autowired
    ImageService imageService;

    @Autowired
    CloudFlareR2Service cloudFlareR2Service;

    @GetMapping("/")
    public ResponseEntity<List<Page>> index() {
        try {
            List<Page> pages = pageService.findAll();
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page> getPageById(@PathVariable Long id) {
        try {
            Optional<Page> optPage = pageService.findById(id);
            if (optPage.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(optPage.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Page> addPage(@RequestBody Page page, @RequestParam("file") MultipartFile image) {
        try {

            String url = cloudFlareR2Service.uploadFile(image);
            Image img = new Image();
            img.setImageUrl(url);
            img.setIsCover(true);
            img.setPage(page);
            img.setTravel(page.getTravel());
            page.setCoverImageUrl(url);
            Page newPage = pageService.create(page);
            return ResponseEntity.ok(newPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removePageById(@PathVariable Long id) {
        try {
            Optional<Page> pageToDelete = pageService.findById(id);
            if (pageToDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            pageService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
