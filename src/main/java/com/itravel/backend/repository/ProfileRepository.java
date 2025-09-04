package com.itravel.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itravel.backend.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {

}
