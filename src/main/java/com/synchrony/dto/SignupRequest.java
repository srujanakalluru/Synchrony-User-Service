package com.synchrony.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    @NotNull(message = "The username cannot be empty")
    private String username;
    @NotNull(message = "The password cannot be empty")
    private String password;
    @NotNull(message = "The first name cannot be empty")
    private String firstName;
    @NotNull(message = "The last name cannot be empty")
    private String lastName;
}