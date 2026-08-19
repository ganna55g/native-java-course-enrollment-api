package com.coursemanagement.dto.request;

import com.coursemanagement.model.PaymentMethod;

public class CreatePaymentRequest {

    private String enrollmentId;
    private PaymentMethod paymentMethod;
    private String paymentReference;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(
            String enrollmentId,
            PaymentMethod paymentMethod,
            String paymentReference) {

        this.enrollmentId = enrollmentId;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
}