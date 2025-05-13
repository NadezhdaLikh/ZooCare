package com.spring.zoocare.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomBackendException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
}
