package com.portfolio.localdev.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final int MAX_MESSAGES = 20;

    private final LinkedList<String> messages = new LinkedList<>();

    @KafkaListener(topics = "demo-topic", groupId = "local-dev-group")
    public void consume(String message) {
        log.info("Received message: {}", message);
        synchronized (messages) {
            messages.addLast(message);
            if (messages.size() > MAX_MESSAGES) {
                messages.removeFirst();
            }
        }
    }

    public List<String> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }
}
