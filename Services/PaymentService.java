package com.coursemanagement.Services;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.mapper.PaymentMapper;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.EnrollmentStatus;
import com.coursemanagement.model.PaymentMethod;
import com.coursemanagement.payment.command.PaymentCommand;
import com.coursemanagement.payment.gateway.PaymentGateway;
import com.coursemanagement.payment.gateway.PaymentGatewayFactory;
import com.coursemanagement.payment.processor.CardPaymentProcessor;
import com.coursemanagement.payment.processor.PaymentProcessor;
import com.coursemanagement.payment.processor.WalletPaymentProcessor;
import com.coursemanagement.repository.interfaces.CourseRepository;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.PaymentRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    private PaymentRepository paymentRepository;
    private EnrollmentRepository enrollmentRepository;
    private CourseRepository courseRepository;
    private PaymentMapper paymentMapper;

    public PaymentService(
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            PaymentMapper paymentMapper) {

        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.paymentMapper = paymentMapper;
    }

    public PaymentResponse createPayment(
            CreatePaymentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment request is required"
            );
        }

        if (request.getEnrollmentId() == null
                || request.getEnrollmentId().isEmpty()) {

            throw new IllegalArgumentException(
                    "Enrollment ID is required"
            );
        }

        if (request.getPaymentMethod() == null) {

            throw new IllegalArgumentException(
                    "Payment method is required"
            );
        }

        if (request.getPaymentReference() == null
                || request.getPaymentReference().isEmpty()) {

            throw new IllegalArgumentException(
                    "Payment reference is required"
            );
        }

        Enrollment enrollment =
                enrollmentRepository.findById(
                        request.getEnrollmentId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Enrollment not found"
                        ));

        if (enrollment.getStatus()
                == EnrollmentStatus.CONFIRMED) {

            throw new IllegalArgumentException(
                    "Enrollment has already been paid"
            );
        }

        if (enrollment.getStatus()
                != EnrollmentStatus.PENDING_PAYMENT) {

            throw new IllegalArgumentException(
                    "Enrollment is not ready for payment"
            );
        }

        if (paymentRepository
                .findByEnrollmentId(
                        request.getEnrollmentId()
                ).isPresent()) {

            throw new IllegalArgumentException(
                    "Payment already exists for this enrollment"
            );
        }

        BigDecimal amount =
                enrollment.getFinalPrice();

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Invalid enrollment final price"
            );
        }

        PaymentCommand command =
                new PaymentCommand.Builder()
                        .enrollmentId(
                                request.getEnrollmentId()
                        )
                        .amount(amount)
                        .paymentMethod(
                                request.getPaymentMethod()
                        )
                        .paymentReference(
                                request.getPaymentReference()
                        )
                        .build();

        PaymentGateway gateway =
                PaymentGatewayFactory.getGateway(
                        request.getPaymentMethod()
                );

        PaymentProcessor processor =
                createProcessor(
                        request.getPaymentMethod(),
                        gateway
                );

        return processor.process(
                command,
                enrollment
        );
    }

    private PaymentProcessor createProcessor(
            PaymentMethod paymentMethod,
            PaymentGateway gateway) {

        switch (paymentMethod) {

            case CREDIT_CARD:
            case DEBIT_CARD:
                return new CardPaymentProcessor(
                        gateway,
                        paymentRepository,
                        enrollmentRepository,
                        courseRepository,
                        paymentMapper
                );

            case PAYPAL:
            case BANK_TRANSFER:
                return new WalletPaymentProcessor(
                        gateway,
                        paymentRepository,
                        enrollmentRepository,
                        courseRepository,
                        paymentMapper
                );

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

    public PaymentResponse findPaymentById(String id) {

        return paymentRepository.findById(id)
                .map(paymentMapper::toResponse)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found"
                        ));
    }

    public Map<String, PaymentResponse> findAllPayments() {

        Map<String, com.coursemanagement.model.Payment> payments =
                paymentRepository.findAll();

        Map<String, PaymentResponse> responses =
                new HashMap<>();

        for (Map.Entry<String, com.coursemanagement.model.Payment> entry
                : payments.entrySet()) {

            responses.put(
                    entry.getKey(),
                    paymentMapper.toResponse(
                            entry.getValue()
                    )
            );
        }

        return responses;
    }
}