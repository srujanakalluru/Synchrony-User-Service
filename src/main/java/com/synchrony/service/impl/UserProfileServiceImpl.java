package com.synchrony.service.impl;

import com.synchrony.cache.UsersCache;
import com.synchrony.client.ImgurApi;
import com.synchrony.entity.Image;
import com.synchrony.entity.User;
import com.synchrony.entity.UserProfile;
import com.synchrony.errorhandling.SynchronyApplicationException;
import com.synchrony.repository.ImageRepository;
import com.synchrony.repository.UserProfileRepository;
import com.synchrony.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.synchrony.utils.Constants.IMAGE_NOT_FOUND_FOR_THE_USER;

@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final ImgurApi imgurApi;
    private final UserProfileRepository userProfileRepository;
    private final ImageRepository imageRepository;
    private final UsersCache usersCache;

    @Autowired
    public UserProfileServiceImpl(ImgurApi imgurApi,
                                  UserProfileRepository userProfileRepository, ImageRepository imageRepository, UsersCache usersCache) {
        this.imgurApi = imgurApi;
        this.userProfileRepository = userProfileRepository;
        this.imageRepository = imageRepository;
        this.usersCache = usersCache;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return usersCache.findByUsername(authentication.getName())
                .orElseThrow(() -> new SynchronyApplicationException("User not found"));
    }


    @Override
    @Transactional
    public Image uploadImage(MultipartFile imageFile, String title) throws IOException {

        User user = getCurrentUser();
        UserProfile userProfile = user.getUserProfile();

        Image image = imgurApi.uploadImage(imageFile, title).getImage();
        image.setUserProfile(userProfile);
        userProfile.getImages().add(image);
        userProfileRepository.save(userProfile);
        imageRepository.save(image);
        return image;
    }

    @Override
    @Transactional
    public void deleteImage(String deleteHash) {
        User user = getCurrentUser();
        UserProfile userProfile = user.getUserProfile();
        List<Image> images = userProfile.getImages();

        Image imageToDelete = images.stream()
                .filter(img -> img.getImageHash().equals(deleteHash))
                .findFirst()
                .orElseThrow(() -> new SynchronyApplicationException(
                        String.format(IMAGE_NOT_FOUND_FOR_THE_USER, deleteHash, user.getUsername())
                ));

        imgurApi.deleteImage(deleteHash);
        userProfile.getImages().remove(imageToDelete);
        imageRepository.delete(imageToDelete);
        userProfileRepository.save(userProfile);
    }

}
