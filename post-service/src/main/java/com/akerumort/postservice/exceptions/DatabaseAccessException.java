package com.akerumort.postservice.exceptions;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(String message) {
        super(message);
    }
}
