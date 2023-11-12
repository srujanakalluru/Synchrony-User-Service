package com.synchrony.errorhandling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynchronyApplicationException extends RuntimeException {
    private final String message;

    public SynchronyApplicationException(String message) {
        this.message = message;
    }

}
