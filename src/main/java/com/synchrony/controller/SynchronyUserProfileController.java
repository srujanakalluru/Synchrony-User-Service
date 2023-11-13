package com.synchrony.controller;

import com.synchrony.dto.UserProfileDTO;
import com.synchrony.entity.Image;
import com.synchrony.service.UserProfileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
@Validated
@Tag(name = "Synchrony User Profile Controller", description = "Synchrony User Profile Controller")
public class SynchronyUserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public SynchronyUserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @ApiOperation(value = "getProfileDetails", nickname = "getProfileDetails", notes = "", response = Image.class, tags = {"Synchrony User Profile Controller",})
    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<UserProfileDTO> getProfileDetails() {
        UserProfileDTO userProfileDTO = userProfileService.getUserProfileDetails();
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);

    }

    @ApiOperation(value = "uploadImage", nickname = "uploadImage", notes = "", response = Image.class, tags = {"Synchrony User Profile Controller",})
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "images", consumes = "multipart/form-data")
    public ResponseEntity<Image> uploadImage(@Valid @RequestPart(name = "file") @NotNull(message = "File cannot be empty") MultipartFile file,
                                             @RequestPart(name = "title") String title) throws IOException {
        Image image = userProfileService.uploadImage(file, title);
        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    @ApiOperation(value = "deleteImage", nickname = "deleteImage", notes = "", tags = {"Synchrony User Profile Controller",})
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/images/{deleteHash}")
    public ResponseEntity<Void> deleteImage(@PathVariable @NotNull(message = "deleteHash cannot be null") @NotNull(message = "DeleteHash must not be empty") String deleteHash) {
        userProfileService.deleteImage(deleteHash);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
