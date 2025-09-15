package com.itravel.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itravel.backend.models.Page;

public interface PageRepository extends JpaRepository<Page, Long> {
    @Query("SELECT p FROM Page p WHERE p.travel.id = :travelId")
    List<Page> findByTravelId(@Param("travelId") Long travelId);

    @Query("SELECT p FROM Page p WHERE p.profile.id = :profileId")
    List<Page> findByProfileId(@Param("profileId") String profileId);

    @Query("SELECT p FROM Page p WHERE p.travel.id = :travelId ORDER BY p.date ASC")
    List<Page> findByTravelIdOrderByDate(@Param("travelId") Long travelId);
}
