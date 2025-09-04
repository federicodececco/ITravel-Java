package com.itravel.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itravel.backend.models.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

}
