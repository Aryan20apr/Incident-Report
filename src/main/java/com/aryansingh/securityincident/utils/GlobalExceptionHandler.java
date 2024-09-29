package com.aryansingh.securityincident.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e){
        ApiResponse<String> apiResponse=new ApiResponse<>(AppConstants.ERROR_MESSAGE,e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extract field errors and create a map of field names and error messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(new ApiResponse(AppConstants.ERROR_MESSAGE,errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleApiException(ApiException e){
        ApiResponse<String> apiResponse=new ApiResponse<>(AppConstants.ERROR_MESSAGE,e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientRolesException.class)
    public ResponseEntity<ApiResponse<String>> handleInsufficientRolesException(InsufficientRolesException e){
        ApiResponse<String> apiResponse=new ApiResponse<>(AppConstants.ACCESS_DENIED_MESSAGE,e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException e){
        ApiResponse<String> apiResponse=new ApiResponse<>("User not authenticated",e.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}


