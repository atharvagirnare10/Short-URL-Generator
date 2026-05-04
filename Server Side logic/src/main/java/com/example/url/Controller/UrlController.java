package com.example.url.Controller;

import com.example.url.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "*")

        @RestController
        public class UrlController {

            @Autowired
            private UrlService urlService;

            @Value("${app.base-url:http://localhost:8080}")
            private String baseUrl;

            @PostMapping("/api/urls")
            public ResponseEntity<?> createShortUrl(@RequestBody Map<String, String> request) {
                String originalUrl = request.get("originalUrl");
                String customAlias = request.get("alias"); // Look for the custom text

                try {
                    // Pass both to the service
                    String shortHash = urlService.shortenUrl(originalUrl, customAlias);
                    String shortUrl = baseUrl + "/" + shortHash;
                    return ResponseEntity.ok(Map.of("shortUrl", shortUrl));

                } catch (IllegalArgumentException e) {
                    // If the alias is taken, return a 400 error message
                    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
            }

            @GetMapping("/{shortHash}")
            public ResponseEntity<Void> redirect(@PathVariable String shortHash) {
                String originalUrl = urlService.getOriginalUrl(shortHash);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(originalUrl))
                        .build();
            }

            @GetMapping("/api/urls/{shortHash}")
            public ResponseEntity<?> getUrlData(@PathVariable String shortHash) {
                String originalUrl = urlService.getOriginalUrl(shortHash);
                String shortUrl = baseUrl + "/" + shortHash;
                return ResponseEntity.ok(Map.of(
                        "shortHash", shortHash,
                        "shortUrl", shortUrl,
                        "originalUrl", originalUrl
                ));
            }
        }