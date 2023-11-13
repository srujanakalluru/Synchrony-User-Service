package com.synchrony.service;

import com.synchrony.dto.UserProfileDTO;
import com.synchrony.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserProfileService {
    Image uploadImage(MultipartFile imageFile, String title) throws IOException;

    void deleteImage(String deleteHash);

    UserProfileDTO getUserProfileDetails();
}
