package com.roadmaps.Roadmaps.common.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RootException{
    public ApiException(String message) {
        super(message, HttpStatus.BAD_REQUEST.toString());
    }

    public ApiException() {
        super("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }
}
