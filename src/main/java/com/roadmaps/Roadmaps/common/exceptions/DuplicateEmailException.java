package com.roadmaps.Roadmaps.common.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends RootException{
    public DuplicateEmailException(String email) {
        super("Account already exist with : " + email, HttpStatus.CONFLICT.toString(), HttpStatus.CONFLICT);
    }
}
