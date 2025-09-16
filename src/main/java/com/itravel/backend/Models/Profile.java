package com.itravel.backend.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    public Profile(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.blacklist = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

}
