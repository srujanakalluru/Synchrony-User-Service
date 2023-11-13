package com.synchrony.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchrony.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String firstName;
    private String lastName;
    private List<Image> imageList;
}
