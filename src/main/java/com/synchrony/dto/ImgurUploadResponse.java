package com.synchrony.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.synchrony.entity.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgurUploadResponse {
    @JsonProperty("data")
    private Image image;
}