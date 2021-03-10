package com.athena.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConflictException extends RuntimeException{
    private String message = "Object already exists.";
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public ConflictException(String message) {
        this.message = message;
    }
}
