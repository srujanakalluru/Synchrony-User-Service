package com.synchrony.controller;

import com.synchrony.dto.SignupRequest;
import com.synchrony.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.synchrony.utils.Constants.USER_REGISTERED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SynchronyUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SynchronyUserController userController;

    @Test
    void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest("dummyUser", "dummyPassword", "firstName", "lastName");
        doNothing().when(userService).registerUser(signupRequest);
        ResponseEntity<String> response = userController.registerUser(signupRequest);
        verify(userService, times(1)).registerUser(signupRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_REGISTERED_SUCCESSFULLY, response.getBody());
    }
}
