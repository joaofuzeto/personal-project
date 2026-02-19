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
    private ResponseEntity<String> UserNotFoundHandler(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    private ResponseEntity<String> CustomerNotFoundHandler(CustomerNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    private ResponseEntity<String> SubscriptionNotFoundHandler(SubscriptionNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assinatura não encontrada");
    }
}
