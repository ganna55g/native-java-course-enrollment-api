package com.coursemanagement.mapper;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.model.Payment;

public class PaymentMapper {

    public Payment toPayment(CreatePaymentRequest request) {

        Payment payment = new Payment();

        payment.setEnrollmentId(request.getEnrollmentId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionReference(request.getTransactionReference());

        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setEnrollmentId(payment.getEnrollmentId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());
        response.setTransactionReference(payment.getTransactionReference());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }
}