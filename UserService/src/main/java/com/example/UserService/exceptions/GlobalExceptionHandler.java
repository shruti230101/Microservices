package com.example.UserService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e)
    {
        String message = e.getMessage();
        APIResponse response = APIResponse.builder()
                                            .message(message)
                                            .success(true)
                                            .status(HttpStatus.NOT_FOUND)
                                            .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<APIResponse> handleRestClientException(RestClientException ex) {
        String message = "Error fetching ratings: " + ex.getMessage();
        APIResponse response = APIResponse.builder()
                                            .message(message)
                                            .success(false)
                                            .status(HttpStatus.SERVICE_UNAVAILABLE)
                                            .build();
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
