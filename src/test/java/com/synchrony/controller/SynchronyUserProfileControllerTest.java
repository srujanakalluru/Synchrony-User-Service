package com.synchrony.controller;

import com.synchrony.dto.UserProfileDTO;
import com.synchrony.entity.Image;
import com.synchrony.entity.UserProfile;
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
import java.util.ArrayList;
import java.util.List;

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
    void getProfileDetails_ReturnsUserProfileDTO() {
        UserProfile userProfile = new UserProfile();
        List<Image> imageList = new ArrayList<>();
        Image img1 = new Image();
        img1.setLink("www.dummy1.com");
        img1.setType("png");
        img1.setTitle("dummy_title_1");
        img1.setId("abc");

        Image img2 = new Image();
        img2.setLink("www.dummy2.com");
        img2.setType("png");
        img2.setTitle("dummy_title_2");
        img2.setId("def");

        imageList.add(img1);
        imageList.add(img2);

        userProfile.setImages(imageList);
        userProfile.setFirstName("firstName");
        userProfile.setLastName("lastName");

        UserProfileDTO expectedUserProfileDTO = new UserProfileDTO();
        expectedUserProfileDTO.setImageList(imageList);
        expectedUserProfileDTO.setFirstName("firstName");
        expectedUserProfileDTO.setLastName("lastName");

        when(userProfileService.getUserProfileDetails()).thenReturn(expectedUserProfileDTO);

        ResponseEntity<UserProfileDTO> responseEntity = userProfileController.getProfileDetails();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUserProfileDTO, responseEntity.getBody());
    }

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
