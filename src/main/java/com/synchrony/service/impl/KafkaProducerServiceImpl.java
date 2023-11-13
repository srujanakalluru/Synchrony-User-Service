package com.synchrony.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements com.synchrony.service.KafkaProducerService {

    private static final String TOPIC_NAME = "user-image";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String username, String imageName) {
        String message = String.format("username: %s, imageName: %s", username, imageName);
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
