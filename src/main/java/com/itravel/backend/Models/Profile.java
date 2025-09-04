package com.itravel.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String avatarUrl;

    private Boolean blacklist;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
