package com.itravel.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itravel.backend.models.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

    @Query("SELECT i FROM Image i WHERE i.travel.id = :travelId")
    List<Image> findByTravelId(@Param("travelId") Long travelId);

    @Query("SELECT i FROM Image i WHERE i.page.id = :pageId")
    List<Image> findByPageId(@Param("pageId") Long pageId);

    @Query("SELECT i FROM Image i WHERE i.travel.id = :travelId AND i.isCover = :isCover")
    List<Image> findByTravelIdAndIsCover(@Param("travelId") Long travelId, @Param("isCover") Boolean isCover);

    @Query("SELECT i FROM Image i WHERE i.page.id = :pageId AND i.isCover = :isCover")
    List<Image> findByPageIdAndIsCover(@Param("pageId") Long pageId, @Param("isCover") Boolean isCover);

    @Query("DELETE FROM Image i WHERE i.travel.id = :travelId")
    void deleteByTravelId(@Param("travelId") Long travelId);

    @Query("DELETE FROM Image i WHERE i.page.id = :pageId")
    void deleteByPageId(@Param("pageId") Long pageId);
}
