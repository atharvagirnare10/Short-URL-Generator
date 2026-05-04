package com.example.url.Service;

import com.example.url.Entity.UrlMap;
import com.example.url.Repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int HASH_LENGTH = 6;

    //accepts a customAlias
    public String shortenUrl(String originalUrl, String customAlias) {
        String hash;

        // 1. Check if the user provided a custom alias
        if (customAlias != null && !customAlias.trim().isEmpty()) {
            // Check if it already exists in the database
            if (urlRepository.findByShortHash(customAlias).isPresent()) {
                // If it exists, throw an error so the controller can tell the user
                throw new IllegalArgumentException("Custom alias '" + customAlias + "' is already taken!");
            }
            hash = customAlias.trim(); // Use the user's custom text
        } else {
            // 2. Auto-generate if no custom alias was provided
            hash = generateHash();
            while (urlRepository.findByShortHash(hash).isPresent()) {
                hash = generateHash();
            }
        }

        // Save to database
        UrlMap urlMap = new UrlMap();
        urlMap.setOriginalUrl(originalUrl);
        urlMap.setShortHash(hash);
        urlRepository.save(urlMap);

        return hash;
    }

    public String getOriginalUrl(String shortHash) {
        return urlRepository.findByShortHash(shortHash)
                .map(UrlMap::getOriginalUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));
    }

    private String generateHash() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(HASH_LENGTH);
        for (int i = 0; i < HASH_LENGTH; i++) {
            sb.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return sb.toString();
    }
}