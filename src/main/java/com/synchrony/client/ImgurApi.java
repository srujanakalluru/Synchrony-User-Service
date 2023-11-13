package com.synchrony.client;

import com.synchrony.dto.ImgurUploadResponse;
import com.synchrony.errorhandling.SynchronyApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static com.synchrony.utils.Constants.API_URL;

@Service
public class ImgurApi {
    private final RestTemplate imgurRestTemplate;

    @Autowired
    public ImgurApi(RestTemplate imgurRestTemplate) {
        this.imgurRestTemplate = imgurRestTemplate;
    }

    public ImgurUploadResponse uploadImage(MultipartFile imageFile, String title) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data; boundary=--------------------------678213828057108133520148");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(imageFile.getBytes(), imageFile.getOriginalFilename()));
        body.add("title", title);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<ImgurUploadResponse> response = imgurRestTemplate.exchange(buildUploadImageUri(), HttpMethod.POST, requestEntity, ImgurUploadResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new SynchronyApplicationException("Failed to upload the image");
    }


    public void deleteImage(String deleteHash) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = imgurRestTemplate.exchange(buildDeleteImageUri(deleteHash), HttpMethod.DELETE, requestEntity, Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new SynchronyApplicationException("Failed to delete image from Imgur");
        }
    }

    private URI buildUploadImageUri() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(API_URL);
        return builder.build().toUri();
    }


    private URI buildDeleteImageUri(String deleteHash) {
        return UriComponentsBuilder.fromUriString("{apiUrl}/{deleteHash}")
                .buildAndExpand(API_URL, deleteHash)
                .toUri();
    }

}
