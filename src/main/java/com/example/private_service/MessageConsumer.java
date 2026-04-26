package com.example.private_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessageRepository repository;
    private final StarwarsClient starwarsClient;
    private final Random random = new Random();

    @KafkaListener(topics = "test-topic", groupId = "private-group")
    public void listen(String message) {
        log.info(">>>> KAFKA CONSUMER: Received message for enrichment: '{}'", message);
        
        try {
            // 1. Enrich with Star Wars character via the Facade Service
            int characterId = random.nextInt(82) + 1;
            log.info(">>>> CALLING FACADE: Fetching character info for ID {} via Feign...", characterId);
            
            Map<String, Object> response = starwarsClient.getCharacter(characterId);
            String characterName = (String) response.get("name");
            
            String enrichedMessage = message + " (Enriched by " + characterName + ")";
            log.info(">>>> ENRICHMENT: Result: '{}'", enrichedMessage);
            
            // 2. Save to Postgres
            MessageEntity entity = new MessageEntity(enrichedMessage);
            repository.save(entity);
            log.info(">>>> DATABASE: Successfully saved enriched message with ID: {}", entity.getId());
            
        } catch (Exception e) {
            log.error(">>>> ERROR: Failed to enrich message: {}", e.getMessage());
            // Fallback: save without enrichment
            repository.save(new MessageEntity(message + " (Enrichment failed: " + e.getMessage() + ")"));
        }
    }
}
