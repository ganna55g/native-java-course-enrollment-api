package com.coursemanagement.payment.gateway;

import com.coursemanagement.model.PaymentMethod;

public class PaymentGatewayFactory {

    public static PaymentGateway getGateway(
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {
            throw new IllegalArgumentException(
                    "Payment method is required"
            );
        }

        switch (paymentMethod) {

            case CREDIT_CARD:
            case DEBIT_CARD:
                return new CardPaymentAdapter();

            case PAYPAL:
                return new WalletPaymentAdapter();

            case BANK_TRANSFER:
                return new BankTransferPaymentAdapter();

            case CASH:
                throw new IllegalArgumentException(
                        "Unsupported payment method"
                );

            default:
                throw new IllegalArgumentException(
                        "Unsupported payment method"
                );
        }
    }
}