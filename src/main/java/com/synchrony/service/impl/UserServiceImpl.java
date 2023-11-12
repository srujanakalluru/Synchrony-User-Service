package com.synchrony.service.impl;

import com.synchrony.dto.SignupRequest;
import com.synchrony.entity.User;
import com.synchrony.entity.UserProfile;
import com.synchrony.errorhandling.SynchronyApplicationException;
import com.synchrony.repository.UserProfileRepository;
import com.synchrony.repository.UserRepository;
import com.synchrony.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        if (existsByUsername(signupRequest.getUsername())) {
            throw new SynchronyApplicationException("Username is already taken");
        }

        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.saveAndFlush(user);

        UserProfile userProfile = new UserProfile(signupRequest.getFirstName(), signupRequest.getLastName());
        userProfile.setUser(user);
        userProfileRepository.saveAndFlush(userProfile);
    }
}