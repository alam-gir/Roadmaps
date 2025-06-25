package com.roadmaps.Roadmaps.common.exceptions;

import com.roadmaps.Roadmaps.common.utils.ApiResponse;
import com.roadmaps.Roadmaps.common.utils.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ApiResponse<Void>> handleRootException(RootException ex, HttpServletRequest request) {
        log.warn(" Root exception : {} - {}", ex.getErrorCode(), ex.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessage(),
                ex.getErrorCode(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map( error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();

        log.warn("Validation Errors for request : {} - {} errors", request.getRequestURI(), validationErrors.size());

        ApiResponse<Void> response = ApiResponse.validationError(
                "Validation failed",
                "VALIDATION_FAILED",
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<ValidationError> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .toList();

        log.warn("Violation error for request : {}", request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.validationError(
                "Validation failed",
                "VALIDATION_FAILED",
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportException (
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request
    ) {
        log.warn(" Method not supported : {} for {}", ex.getMethod(), request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.error(
                ex.getMethod().toString() + " is not supported",
                "METHOD_NOT_SUPPORTED",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access not allowed for : {}", request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.error(
                "Access denied - You have no permission to access this.",
                "NOT_ENOUGH_PERMISSIONS",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateEmailException(
            DuplicateEmailException ex, HttpServletRequest request
    ) {
        log.error("Data integrity error for requestt : {}", request.getRequestURI());

        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessage(),
                "DATABASE_ERROR",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request
    ) {
        log.error("Data integrity error for requestt : {}", request.getRequestURI());

        String message = "Data integrity violation";
        if(ex.getCause() instanceof ConstraintViolationException){
            message = "Duplicate entry error or constraint violation";
        }

        ApiResponse<Void> response = ApiResponse.error(
                message,
                "DATABASE_ERROR",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error(" Unexpected error for {} - {}", request.getRequestURI(), ex.getMessage());

        ApiResponse<Void> response = ApiResponse.error(
                "Unexpected error",
                "INTERNAL_SERVER_ERROR",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
