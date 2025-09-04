package com.itravel.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itravel.backend.models.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {

}
