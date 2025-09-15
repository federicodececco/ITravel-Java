package com.itravel.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itravel.backend.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {
    @Query("SELECT p FROM Profile p WHERE p.email = :email")
    Optional<Profile> findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Profile p WHERE p.username = :username")
    Optional<Profile> findByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Profile p WHERE p.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Profile p WHERE p.username = :username")
    boolean existsByUsername(@Param("username") String username);
}
