package com.itravel.backend.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    @JsonBackReference
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "page_id")
    @JsonBackReference
    private Page page;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
