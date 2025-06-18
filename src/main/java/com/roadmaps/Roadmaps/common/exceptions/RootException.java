package com.roadmaps.Roadmaps.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RootException extends RuntimeException{
    public final String errorCode;
    public final HttpStatus httpStatus;

    public RootException (String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public RootException (String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
