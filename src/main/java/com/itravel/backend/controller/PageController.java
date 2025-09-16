package com.itravel.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itravel.backend.models.Page;
import com.itravel.backend.models.Profile;
import com.itravel.backend.models.Travel;
import com.itravel.backend.service.CloudFlareR2Service;
import com.itravel.backend.service.ImageService;
import com.itravel.backend.service.PageService;
import com.itravel.backend.service.ProfileService;
import com.itravel.backend.service.TravelService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pages")
@CrossOrigin(origins = "*")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TravelService travelService;

    @GetMapping("/")
    public ResponseEntity<?> index() {
        try {
            List<Page> pages = pageService.findAll();
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving pages");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable Long id) {
        try {
            Optional<Page> optPage = pageService.findById(id);
            if (optPage.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(optPage.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving page");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> addPage(@RequestBody Page page) {
        try {

            if (page == null) {
                return ResponseEntity.badRequest().body("Page data is required");
            }

            if (page.getTravel() == null) {
                return ResponseEntity.badRequest().body("Travel information is required");
            }

            if (page.getTravel().getId() == null) {
                return ResponseEntity.badRequest().body("Travel ID is required");
            }

            Optional<Travel> optTravel = travelService.findById(page.getTravel().getId());
            if (optTravel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("travel not found with id" + page.getTravel().getId());
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            Profile userProfile = profileService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            Travel travel = optTravel.get();
            if (userProfile.getId() != travel.getProfile().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User and travel profile mismatch, you cannot create a page for a travel you don't own");
            }

            page.setTravel(travel);
            page.setProfile(userProfile);
            Page newPage = pageService.create(page);

            return ResponseEntity.ok(newPage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating page: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePage(@PathVariable Long id, @RequestBody Page page) {
        try {
            Optional<Page> existingPage = pageService.findById(id);
            if (existingPage.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            page.setId(id);
            Page updatedPage = pageService.create(page);
            return ResponseEntity.ok(updatedPage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating page");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removePageById(@PathVariable Long id) {
        try {
            Optional<Page> pageToDelete = pageService.findById(id);
            if (pageToDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            pageService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting page");
        }
    }
}