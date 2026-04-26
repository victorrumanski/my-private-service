package com.example.private_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/internal")
public class StatusController {

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
            "status", "UP",
            "service", "my-private-service",
            "enrichment", "Star Wars API (swapi.dev)",
            "visibility", "PRIVATE (Internal only)"
        );
    }
}
