package com.project.sales_api.infra;

import com.project.sales_api.exception.CustomerNotFoundException;
import com.project.sales_api.exception.SubscriptionNotFoundException;
import com.project.sales_api.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<RestErrorMessage> UserNotFoundHandler(UserNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.getHttpStatus()).body(threatResponse);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    private ResponseEntity<RestErrorMessage> CustomerNotFoundHandler(CustomerNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.getHttpStatus()).body(threatResponse);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    private ResponseEntity<RestErrorMessage> SubscriptionNotFoundHandler(SubscriptionNotFoundException exception){
        RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(threatResponse.getHttpStatus()).body(threatResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception){

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}
