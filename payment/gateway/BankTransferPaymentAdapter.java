package com.coursemanagement.payment.gateway;

import com.coursemanagement.payment.command.PaymentCommand;

public class BankTransferPaymentAdapter implements PaymentGateway {

    @Override
    public boolean processPayment(PaymentCommand command) {

        System.out.println(
                "Processing bank transfer using external bank provider"
        );

        return command.getPaymentReference() != null
                && !command.getPaymentReference().isEmpty();
    }
}