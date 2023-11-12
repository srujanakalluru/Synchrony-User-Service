package com.synchrony.service;

import com.synchrony.dto.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean existsByUsername(String username);

    void registerUser(SignupRequest signupRequest);
}
