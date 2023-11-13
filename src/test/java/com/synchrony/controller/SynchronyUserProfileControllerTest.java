package com.synchrony.controller;

import com.synchrony.entity.Image;
import com.synchrony.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SynchronyUserProfileControllerTest {
    private static final String TITLE = "test-title";

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private SynchronyUserProfileController userProfileController;

    @Test
    void testUploadImage() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        Image testImage = new Image();

        when(userProfileService.uploadImage(file, TITLE)).thenReturn(testImage);
        ResponseEntity<Image> response = userProfileController.uploadImage(file, TITLE);

        verify(userProfileService, times(1)).uploadImage(file, TITLE);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testImage, response.getBody());
    }

    @Test
    void testDeleteImage() {
        String deleteHash = "deleteHash";

        doNothing().when(userProfileService).deleteImage(deleteHash);
        ResponseEntity<Void> response = userProfileController.deleteImage(deleteHash);

        verify(userProfileService, times(1)).deleteImage(deleteHash);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
