package com.coursemanagement.payment.processor;

import com.coursemanagement.mapper.PaymentMapper;
import com.coursemanagement.payment.gateway.PaymentGateway;
import com.coursemanagement.repository.interfaces.CourseRepository;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.PaymentRepository;

public class CardPaymentProcessor extends PaymentProcessor {

    public CardPaymentProcessor(
            PaymentGateway paymentGateway,
            PaymentRepository paymentRepository,
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            PaymentMapper paymentMapper) {

        super(
                paymentGateway,
                paymentRepository,
                enrollmentRepository,
                courseRepository,
                paymentMapper
        );
    }

    @Override
    protected void performPostPaymentActions(
            com.coursemanagement.payment.command.PaymentCommand command,
            com.coursemanagement.model.Enrollment enrollment) {

        System.out.println(
                "Card payment completed successfully"
        );
    }
}