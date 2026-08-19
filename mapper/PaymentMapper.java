package com.coursemanagement.mapper;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.model.Payment;

import java.math.BigDecimal;

public class PaymentMapper {

    public Payment toPayment(
            CreatePaymentRequest request,
            BigDecimal amount) {

        Payment payment = new Payment();

        payment.setEnrollmentId(
                request.getEnrollmentId()
        );

        payment.setAmount(amount);

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        payment.setTransactionReference(
                request.getPaymentReference()
        );

        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {

        PaymentResponse response =
                new PaymentResponse();

        response.setId(payment.getId());

        response.setEnrollmentId(
                payment.getEnrollmentId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setPaymentStatus(
                payment.getPaymentStatus()
        );

        response.setTransactionReference(
                payment.getTransactionReference()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        return response;
    }
}