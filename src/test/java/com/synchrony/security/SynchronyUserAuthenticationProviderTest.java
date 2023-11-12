package com.synchrony.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SynchronyUserAuthenticationProviderTest {

    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private SynchronyUserAuthenticationProvider authenticationProvider;

    @Test
    void testAuthenticateSuccess() {
        UserDetails userDetails = new User(VALID_USERNAME, VALID_PASSWORD, Collections.emptyList());
        when(userDetailsService.loadUserByUsername(VALID_USERNAME)).thenReturn(userDetails);
        when(passwordEncoder.matches(VALID_PASSWORD, userDetails.getPassword())).thenReturn(true);

        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(VALID_USERNAME, VALID_PASSWORD));

        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals(Collections.singleton(new SimpleGrantedAuthority("USER")), new HashSet<>(authentication.getAuthorities()));
    }

    @Test
    void testAuthenticateFailureInvalidUser() {
        when(userDetailsService.loadUserByUsername(VALID_USERNAME)).thenReturn(null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(VALID_USERNAME, VALID_PASSWORD);

        assertThrows(BadCredentialsException.class, () ->
                authenticationProvider.authenticate(usernamePasswordAuthenticationToken)
        );
    }


    @Test
    void testAuthenticateFailureInvalidPassword() {
        UserDetails userDetails = new User(VALID_USERNAME, "wrongPassword", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(VALID_USERNAME)).thenReturn(userDetails);
        when(passwordEncoder.matches(VALID_PASSWORD, userDetails.getPassword())).thenReturn(false);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(VALID_USERNAME, VALID_PASSWORD);
        assertThrows(BadCredentialsException.class, () ->
                authenticationProvider.authenticate(usernamePasswordAuthenticationToken)
        );
    }


    @Test
    void testSupports() {
        assertTrue(authenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(authenticationProvider.supports(Object.class));
    }
}
