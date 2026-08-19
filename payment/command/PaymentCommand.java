package com.coursemanagement.payment.command;

import com.coursemanagement.model.PaymentMethod;

import java.math.BigDecimal;

public class PaymentCommand {

    private final String enrollmentId;
    private final BigDecimal amount;
    private final PaymentMethod paymentMethod;
    private final String paymentReference;

    private PaymentCommand(Builder builder) {

        this.enrollmentId = builder.enrollmentId;
        this.amount = builder.amount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentReference = builder.paymentReference;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public static class Builder {

        private String enrollmentId;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String paymentReference;

        public Builder enrollmentId(String enrollmentId) {
            this.enrollmentId = enrollmentId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder paymentReference(String paymentReference) {
            this.paymentReference = paymentReference;
            return this;
        }

        public PaymentCommand build() {

            if (enrollmentId == null || enrollmentId.isEmpty()) {
                throw new IllegalArgumentException(
                        "Enrollment ID is required"
                );
            }

            if (amount == null
                    || amount.compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Amount must be greater than zero"
                );
            }

            if (paymentMethod == null) {
                throw new IllegalArgumentException(
                        "Payment method is required"
                );
            }

            return new PaymentCommand(this);
        }
    }
}