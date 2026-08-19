package com.coursemanagement.payment.gateway;

import com.coursemanagement.payment.command.PaymentCommand;

public class WalletPaymentAdapter implements PaymentGateway {

    @Override
    public boolean processPayment(PaymentCommand command) {

        System.out.println(
                "Processing wallet payment using external wallet provider"
        );

        return command.getPaymentReference() != null
                && !command.getPaymentReference().isEmpty();
    }
}