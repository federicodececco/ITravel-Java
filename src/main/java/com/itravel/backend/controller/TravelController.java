package com.itravel.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itravel.backend.models.Travel;
import com.itravel.backend.service.TravelService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @GetMapping("/")
    public ResponseEntity<List<Travel>> index() {
        try {
            List<Travel> travels = travelService.findAll();
            return ResponseEntity.ok(travels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getTravelById(@PathVariable Long id) {
        try {
            Optional<Travel> optTravel = travelService.findById(id);
            if (optTravel.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(optTravel.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Travel> addTravel(@RequestBody Travel travel) {
        try {
            Travel newTravel = travelService.create(travel);
            return ResponseEntity.ok(newTravel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> removeTravelById(@PathVariable Long id) {
        try {
            Optional<Travel> travelToDelete = travelService.findById(id);
            if (travelToDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            travelService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}