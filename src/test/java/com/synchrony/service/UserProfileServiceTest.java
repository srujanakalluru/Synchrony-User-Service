package com.synchrony.service;

import com.synchrony.cache.UsersCache;
import com.synchrony.client.ImgurApi;
import com.synchrony.dto.ImgurUploadResponse;
import com.synchrony.entity.Image;
import com.synchrony.entity.User;
import com.synchrony.entity.UserProfile;
import com.synchrony.errorhandling.SynchronyApplicationException;
import com.synchrony.repository.ImageRepository;
import com.synchrony.repository.UserProfileRepository;
import com.synchrony.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.synchrony.utils.Constants.IMAGE_NOT_FOUND_FOR_THE_USER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {
    private static final String TITLE = "test-title";

    @Mock
    private ImgurApi imgurApi;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UsersCache usersCache;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    void setUp() {
        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", "testPassword", Collections.singleton(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testUploadImage_Success() throws IOException {
        MultipartFile mockImageFile = mock(MultipartFile.class);
        User mockUser = mock(User.class);
        UserProfile mockUserProfile = mock(UserProfile.class);
        Image mockImage = mock(Image.class);
        ImgurUploadResponse imgurUploadResponse = mock(ImgurUploadResponse.class);

        when(usersCache.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(mockUser.getUserProfile()).thenReturn(mockUserProfile);
        when(imgurUploadResponse.getImage()).thenReturn(mockImage);
        when(imgurApi.uploadImage(mockImageFile, TITLE)).thenReturn(imgurUploadResponse);
        when(imageRepository.save(mockImage)).thenReturn(mockImage);

        Image result = userProfileService.uploadImage(mockImageFile, TITLE);

        assertNotNull(result);
        verify(usersCache, times(1)).findByUsername(anyString());
        verify(imgurApi, times(1)).uploadImage(mockImageFile, TITLE);
        verify(imageRepository, times(1)).save(mockImage);
    }

    @Test
    void testUploadImage_UserNotFound() throws IOException {
        MultipartFile mockImageFile = mock(MultipartFile.class);

        when(usersCache.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(SynchronyApplicationException.class, () -> userProfileService.uploadImage(mockImageFile, TITLE));
        verify(imgurApi, never()).uploadImage(any(), anyString());
        verify(imageRepository, never()).save(any());
    }

    @Test
    void testDeleteImage_Success() {
        String deleteHash = "testDeleteHash";
        User mockUser = mock(User.class);
        UserProfile mockUserProfile = mock(UserProfile.class);
        Image mockImage = mock(Image.class);

        when(usersCache.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(mockUser.getUserProfile()).thenReturn(mockUserProfile);
        List<Image> mockImagesList = new ArrayList<>();
        mockImagesList.add(mockImage);
        when(mockUserProfile.getImages()).thenReturn(mockImagesList);

        when(mockImage.getImageHash()).thenReturn(deleteHash);
        doNothing().when(imgurApi).deleteImage(deleteHash);

        assertDoesNotThrow(() -> userProfileService.deleteImage(deleteHash));

        verify(usersCache).findByUsername(anyString());
        verify(imgurApi).deleteImage(deleteHash);
        verify(imageRepository).delete(mockImage);
        verify(userProfileRepository).save(mockUserProfile);
    }

    @Test
    void testDeleteImageImageNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        UserProfile userProfile = new UserProfile();
        List<Image> images = new ArrayList<>();
        userProfile.setImages(images);
        user.setUserProfile(userProfile);

        when(usersCache.findByUsername("testUser")).thenReturn(Optional.of(user));
        String deleteHash = "nonexistentHash";
        SynchronyApplicationException exception = assertThrows(SynchronyApplicationException.class, () -> {
            userProfileService.deleteImage(deleteHash);
        });

        String expectedErrorMessage = String.format(IMAGE_NOT_FOUND_FOR_THE_USER, deleteHash, "testUser");
        assert (expectedErrorMessage.equals(exception.getMessage()));
    }


}
