package com.coursemanagement.payment.processor;

import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.mapper.PaymentMapper;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.Payment;
import com.coursemanagement.model.PaymentStatus;
import com.coursemanagement.payment.command.PaymentCommand;
import com.coursemanagement.payment.gateway.PaymentGateway;
import com.coursemanagement.repository.interfaces.CourseRepository;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.PaymentRepository;

import java.time.LocalDateTime;

public abstract class PaymentProcessor {

    protected PaymentGateway paymentGateway;
    protected PaymentRepository paymentRepository;
    protected EnrollmentRepository enrollmentRepository;
    protected CourseRepository courseRepository;
    protected PaymentMapper paymentMapper;

    protected PaymentProcessor(
            PaymentGateway paymentGateway,
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            PaymentMapper paymentMapper) {

        this.paymentGateway = paymentGateway;
        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.paymentMapper = paymentMapper;
    }

    public final PaymentResponse process(
            PaymentCommand command,
            Enrollment enrollment) {

        validateRequest(command, enrollment);

        boolean success =
                executeProviderPayment(command);

        if (!success) {

            handleFailedPayment(command);

            throw new IllegalArgumentException(
                    "Payment failed"
            );
        }

        Payment payment =
                createPaymentRecord(command);

        updateEnrollment(enrollment);

        performPostPaymentActions(
                command,
                enrollment
        );

        return paymentMapper.toResponse(payment);
    }

    protected void validateRequest(
            PaymentCommand command,
            Enrollment enrollment) {

        if (command == null) {

            throw new IllegalArgumentException(
                    "Payment command is required"
            );
        }

        if (enrollment == null) {

            throw new IllegalArgumentException(
                    "Enrollment is required"
            );
        }

        if (command.getAmount() == null
                || command.getAmount()
                .compareTo(java.math.BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        if (enrollment.getFinalPrice() == null
                || !command.getAmount()
                .equals(enrollment.getFinalPrice())) {

            throw new IllegalArgumentException(
                    "Payment amount must equal enrollment final price"
            );
        }
    }

    protected boolean executeProviderPayment(
            PaymentCommand command) {

        return paymentGateway.processPayment(command);
    }

    protected Payment createPaymentRecord(
            PaymentCommand command) {

        Payment payment = new Payment();

        payment.setEnrollmentId(
                command.getEnrollmentId()
        );

        payment.setAmount(
                command.getAmount()
        );

        payment.setPaymentMethod(
                command.getPaymentMethod()
        );

        payment.setTransactionReference(
                command.getPaymentReference()
        );

        payment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        payment.setPaymentDate(
                LocalDateTime.now()
        );

        return paymentRepository.save(payment);
    }

    protected void updateEnrollment(
            Enrollment enrollment) {

        enrollment.setStatus(
                com.coursemanagement.model.EnrollmentStatus.CONFIRMED
        );

        enrollmentRepository.save(enrollment);
    }

    protected void performPostPaymentActions(
            PaymentCommand command,
            Enrollment enrollment) {

    }

    protected void handleFailedPayment(
            PaymentCommand command) {

        System.out.println(
                "Payment failed for enrollment: "
                        + command.getEnrollmentId()
        );
    }
}