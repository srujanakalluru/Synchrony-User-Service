package com.synchrony.errorhandling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        when(methodArgumentTypeMismatchException.getMessage()).thenReturn("Method argument type mismatch");

        ResponseEntity<GlobalError> response = globalExceptionHandler.handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException);

        verify(methodArgumentTypeMismatchException, times(1)).getMessage();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Method argument type mismatch", response.getBody().getErrorReason());
    }

    @Test
    void testSynchronyApplicationException() {
        SynchronyApplicationException synchronyApplicationException = new SynchronyApplicationException("Application exception");

        ResponseEntity<GlobalError> response = globalExceptionHandler.synchronyApplicationException(synchronyApplicationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Application exception", response.getBody().getErrorReason());
    }

    @Test
    void testHandleUnhandledException() {
        Exception exception = new Exception("Unhandled exception");

        ResponseEntity<GlobalError> response = globalExceptionHandler.handleUnhandledException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unhandled exception", response.getBody().getErrorReason());
    }
}
