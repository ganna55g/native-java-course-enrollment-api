package com.coursemanagement.payment.gateway;

import com.coursemanagement.payment.command.PaymentCommand;

public interface PaymentGateway {

    boolean processPayment(PaymentCommand command);
}