package com.synchrony.service;

public interface KafkaProducerService {
    void sendMessage(String username, String imageName);
}
