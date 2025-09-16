package com.itravel.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itravel.backend.models.Profile;
import com.itravel.backend.models.Travel;
import com.itravel.backend.security.JwtUtil;
import com.itravel.backend.service.ProfileService;
import com.itravel.backend.service.TravelService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/travels")
@CrossOrigin(origins = "*")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public ResponseEntity<?> index() {
        try {
            List<Travel> travels = travelService.findAll();
            return ResponseEntity.ok(travels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving travels");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelById(@PathVariable Long id) {
        try {
            Optional<Travel> optTravel = travelService.findById(id);
            if (optTravel.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(optTravel.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving travel");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> addTravel(@RequestBody Travel travel) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            Profile userProfile = profileService.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            if (travel.getTitle() == null || travel.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Travel title is required");
            }

            if (travel.getLocation() == null || travel.getLocation().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Travel location is required");
            }
            travel.setProfile(userProfile);
            Travel newTravel = travelService.create(travel);
            return ResponseEntity.ok(newTravel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating travel: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable Long id, @RequestBody Travel travel) {
        try {
            Optional<Travel> existingTravel = travelService.findById(id);
            if (existingTravel.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (travel.getTitle() == null || travel.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Travel title is required");
            }

            travel.setId(id);
            Travel updatedTravel = travelService.edit(travel);
            return ResponseEntity.ok(updatedTravel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating travel");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> removeTravelById(@PathVariable Long id) {
        try {
            Optional<Travel> travelToDelete = travelService.findById(id);
            if (travelToDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            travelService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting travel");
        }
    }
}