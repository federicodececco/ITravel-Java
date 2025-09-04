package com.itravel.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itravel.backend.models.Travel;
import com.itravel.backend.repository.TravelRepository;

@Service
public class TravelService {
    @Autowired
    TravelRepository travelRepository;

    public List<Travel> findAll() {
        return travelRepository.findAll();
    }

    public Optional<Travel> findById(Long id) {
        return travelRepository.findById(id);
    }

    public Travel create(Travel travel) {
        return travelRepository.save(travel);
    }

    public Travel edit(Travel travel) {
        return travelRepository.save(travel);
    }

    public void deleteById(Long id) {
        travelRepository.deleteById(id);
    }
}
