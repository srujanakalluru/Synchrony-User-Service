package com.synchrony.controller;

import com.synchrony.dto.SignupRequest;
import com.synchrony.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

import static com.synchrony.utils.Constants.USER_REGISTERED_SUCCESSFULLY;

@Validated
@RestController
@RequestMapping("/user")
@Tag(name = "Synchrony User Controller", description = "Synchrony User Controller")
public class SynchronyUserController {
    private final UserService userService;

    public SynchronyUserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "registerUser", nickname = "registerUser", notes = "", response = String.class, tags = {"Synchrony User Controller",})
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @NotNull SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseEntity.ok(USER_REGISTERED_SUCCESSFULLY);
    }

}