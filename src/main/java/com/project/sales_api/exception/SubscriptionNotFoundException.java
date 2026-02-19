package com.project.sales_api.exception;

public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(){
        super("Assinatura não encontrada");
    }

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
