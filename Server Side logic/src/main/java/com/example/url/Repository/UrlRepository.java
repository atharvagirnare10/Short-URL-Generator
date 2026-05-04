package com.example.url.Repository;

import com.example.url.Entity.UrlMap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMap, Long> {
    Optional<UrlMap> findByShortHash(String shortHash);
}