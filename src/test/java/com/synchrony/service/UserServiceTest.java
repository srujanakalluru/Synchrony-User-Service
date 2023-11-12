package com.synchrony.service;

import com.synchrony.dto.SignupRequest;
import com.synchrony.entity.User;
import com.synchrony.entity.UserProfile;
import com.synchrony.errorhandling.SynchronyApplicationException;
import com.synchrony.repository.UserProfileRepository;
import com.synchrony.repository.UserRepository;
import com.synchrony.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void testLoadUserByUsername_Success() {
        String username = "testUser";
        User mockUser = new User(username, "encodedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testExistsByUsername_UserExists() {
        String existingUsername = "existingUser";
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(new User()));

        boolean exists = userService.existsByUsername(existingUsername);

        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername(existingUsername);
    }

    @Test
    void testExistsByUsername_UserDoesNotExist() {
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        boolean exists = userService.existsByUsername(nonExistingUsername);

        assertFalse(exists);
        verify(userRepository, times(1)).findByUsername(nonExistingUsername);
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest("testUser", "password", "John", "Doe");
        when(userRepository.findByUsername(signupRequest.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> userService.registerUser(signupRequest));

        verify(userRepository, times(1)).findByUsername(signupRequest.getUsername());
        verify(passwordEncoder, times(1)).encode(signupRequest.getPassword());
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(userProfileRepository, times(1)).saveAndFlush(any(UserProfile.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyTaken() {
        SignupRequest signupRequest = new SignupRequest("existingUser", "password", "John", "Doe");
        when(userRepository.findByUsername(signupRequest.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(SynchronyApplicationException.class, () -> userService.registerUser(signupRequest));
        verify(userRepository, times(1)).findByUsername(signupRequest.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(userProfileRepository, never()).saveAndFlush(any(UserProfile.class));
    }
}
