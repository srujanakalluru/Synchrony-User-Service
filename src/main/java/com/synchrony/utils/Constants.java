package com.synchrony.utils;

public class Constants {
    public static final String API_URL = "https://api.imgur.com/3/image";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully!";
    public static final String IMAGE_NOT_FOUND_FOR_THE_USER = "Image not found for the delete hash %s for the user: %s";

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

}
