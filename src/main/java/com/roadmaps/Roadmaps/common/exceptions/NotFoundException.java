package com.roadmaps.Roadmaps.common.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RootException{
    public NotFoundException (String message) {
        super(message, HttpStatus.NOT_FOUND.toString(), HttpStatus.NOT_FOUND);
    }

    public NotFoundException() {
        super("Resource not found", HttpStatus.NOT_FOUND.toString(), HttpStatus.NOT_FOUND);
    }
}
