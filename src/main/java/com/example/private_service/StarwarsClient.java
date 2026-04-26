package com.example.private_service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

// We use the internal Kubernetes DNS name for the service
@FeignClient(name = "starwars-client", url = "http://starwars-api-service.apps.svc.cluster.local")
public interface StarwarsClient {

    @GetMapping("/api/swapi/character/{id}")
    Map<String, Object> getCharacter(@PathVariable("id") int id);
}
