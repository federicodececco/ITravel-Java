package com.itravel.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itravel.backend.models.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

}
