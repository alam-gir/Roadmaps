package com.roadmaps.Roadmaps.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ApiResponse<T> {
    private boolean isSuccess;
    private String message;
    private T data;
    private String errorCode;
    private String timeStamp;
    private String path;
    private List<ValidationError> validationErrors;

    // constructor for success
    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
        this.isSuccess = true;
        this.timeStamp = Instant.now().toString();
    }

    // constructor for error
    public ApiResponse(String message, String errorCode, String path) {
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
        this.isSuccess = false;
        this.timeStamp = Instant.now().toString();
    }

    // constructor for validation error
    public ApiResponse(String message, String errorCode, String path, List<ValidationError> validationErrors){
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
        this.validationErrors = validationErrors;
        this.isSuccess = false;
        this.timeStamp = Instant.now().toString();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    public static <T> ApiResponse<T> error (String message, String errorCode, String path) {
        return new ApiResponse<>(message, errorCode, path);
    }

    public static <T> ApiResponse<T> validationError(String message, String errorCode, String path, List<ValidationError> errors) {
        return new ApiResponse<>(message, errorCode, path, errors );
    }
}
