package com.itravel.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itravel.backend.models.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("SELECT t FROM Travel t WHERE t.profile.id = :profileId")
    List<Travel> findByProfileId(@Param("profileId") String profileId);

    @Query("SELECT t FROM Travel t WHERE t.profile.id = :profileId ORDER BY t.startDate DESC")
    List<Travel> findByProfileIdOrderByStartDateDesc(@Param("profileId") String profileId);

    @Query("SELECT t FROM Travel t WHERE LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Travel> findByLocationContainingIgnoreCase(@Param("location") String location);

    @Query("SELECT t FROM Travel t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Travel> findByTitleContainingIgnoreCase(@Param("title") String title);
}
