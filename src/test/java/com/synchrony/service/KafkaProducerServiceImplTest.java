package com.synchrony.service;

import com.synchrony.service.impl.KafkaProducerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceImplTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    @Test
    void testSendMessage() {
        String username = "testUser";
        String imageName = "testImage";
        
        kafkaProducerService.sendMessage(username, imageName);

        String expectedMessage = String.format("username: %s, imageName: %s", username, imageName);
        Mockito.verify(kafkaTemplate).send("user-image", expectedMessage);
    }
}
