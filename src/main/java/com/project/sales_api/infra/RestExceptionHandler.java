package com.project.sales_api.infra;

import com.project.sales_api.exception.CustomerNotFoundException;
import com.project.sales_api.exception.SubscriptionNotFoundException;
import com.project.sales_api.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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
}
