package com.roadmaps.Roadmaps.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends RootException{
    public InvalidTokenException() {
        super("Invalid token! Login again", HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
    }

    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED);
    }
}
