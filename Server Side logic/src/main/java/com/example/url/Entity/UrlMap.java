package com.example.url.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class UrlMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048) // URLs can be long
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10)
    private String shortHash;

    private LocalDateTime createdAt = LocalDateTime.now();
}