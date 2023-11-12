package com.synchrony.client;

import com.synchrony.dto.ImgurUploadResponse;
import com.synchrony.errorhandling.SynchronyApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImgurApiTest {

    @Mock
    private RestTemplate imgurRestTemplate;

    @InjectMocks
    private ImgurApi imgurApi;

    @Test
    void testUploadImage_Success() throws IOException {
        byte[] fileContent = "test image content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", fileContent);

        ImgurUploadResponse expectedResponse = new ImgurUploadResponse();
        ResponseEntity<ImgurUploadResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(imgurRestTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(ImgurUploadResponse.class)))
                .thenReturn(responseEntity);

        ImgurUploadResponse result = imgurApi.uploadImage(file);

        assertEquals(expectedResponse, result);
    }

    @Test
    void testUploadImage_Failure() throws IOException {
        byte[] fileContent = "test image content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", fileContent);

        ResponseEntity<ImgurUploadResponse> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(imgurRestTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(ImgurUploadResponse.class)))
                .thenReturn(responseEntity);

        assertThrows(SynchronyApplicationException.class, () -> imgurApi.uploadImage(file));
    }

    @Test
    void testDeleteImage_Success() {
        String deleteHash = "abc123";
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(imgurRestTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);
        assertDoesNotThrow(() -> imgurApi.deleteImage(deleteHash));
    }

    @Test
    void testDeleteImage_Failure() {
        // Arrange
        String deleteHash = "abc123";
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(imgurRestTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(responseEntity);
        assertThrows(RuntimeException.class, () -> imgurApi.deleteImage(deleteHash));
    }
}
