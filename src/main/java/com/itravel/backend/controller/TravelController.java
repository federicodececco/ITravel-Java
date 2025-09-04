package com.itravel.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itravel.backend.models.Travel;
import com.itravel.backend.service.TravelService;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    TravelService travelService;

    @GetMapping("/")
    public Mono<ResponseEntity<List<Travel>>> index() {
        try {
            List<Travel> res = travelService.findAll();
            return Mono.just(new ResponseEntity<>(res, HttpStatus.OK));
        } catch (Exception e) {
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Travel>> getTravelById(@PathVariable Long id) {
        try {
            Optional<Travel> optTravel = travelService.findById(id);
            if (optTravel.isEmpty()) {
                return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
            return Mono.just(new ResponseEntity<>(optTravel.get(), HttpStatus.OK));

        } catch (Exception e) {
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<Travel>> addPlace(@RequestBody Travel travel) {

        try {
            Travel newTravel = travelService.create(travel);
            return Mono.just(new ResponseEntity<>(newTravel, HttpStatus.OK));
        } catch (Exception e) {
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Travel>> removePlaceById(@PathVariable Long id) {
        try {
            Optional<Travel> travelToDelete = travelService.findById(id);
            if (travelToDelete.isEmpty()) {
                return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
            travelService.deleteById(id);
            return Mono.just(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}