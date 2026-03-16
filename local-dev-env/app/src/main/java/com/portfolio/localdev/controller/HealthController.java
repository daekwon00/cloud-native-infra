package com.portfolio.localdev.controller;

import com.portfolio.localdev.kafka.KafkaConsumerService;
import com.portfolio.localdev.kafka.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final KafkaProducerService producerService;
    private final KafkaConsumerService consumerService;

    public HealthController(KafkaProducerService producerService, KafkaConsumerService consumerService) {
        this.producerService = producerService;
        this.consumerService = consumerService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "service", "local-dev-app"
        ));
    }

    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "");
        producerService.send(message);
        return ResponseEntity.ok(Map.of("status", "sent", "message", message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMessages() {
        return ResponseEntity.ok(consumerService.getMessages());
    }
}
