package com.coursemanagement.payment.gateway;

import com.coursemanagement.payment.command.PaymentCommand;

public class CardPaymentAdapter implements PaymentGateway {

    @Override
    public boolean processPayment(PaymentCommand command) {

        System.out.println(
                "Processing card payment using external card provider"
        );

        return command.getPaymentReference() != null
                && !command.getPaymentReference().isEmpty();
    }
}