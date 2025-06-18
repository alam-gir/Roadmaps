package com.roadmaps.Roadmaps.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEmailPasswordException extends RootException{
    public InvalidEmailPasswordException() {
        super("Invalid email or password!, Try again with authentic credentials.", HttpStatus.UNAUTHORIZED.toString(),  HttpStatus.UNAUTHORIZED);
    }
}
