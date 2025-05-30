package com.example.newsfeed.common.exception;


import java.time.LocalDateTime;

public class ErrorResponse {
    public LocalDateTime time = LocalDateTime.now();
    public String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
