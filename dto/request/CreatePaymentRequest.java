package com.coursemanagement.dto.request;

import com.coursemanagement.model.PaymentMethod;

import java.math.BigDecimal;

public class CreatePaymentRequest {

    private String enrollmentId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String transactionReference;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(String enrollmentId,
                                BigDecimal amount,
                                PaymentMethod paymentMethod,
                                String transactionReference) {
        this.enrollmentId = enrollmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}