package com.coursemanagement.Services;

import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.mapper.PaymentMapper;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.Payment;
import com.coursemanagement.model.PaymentStatus;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.PaymentRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    private PaymentRepository paymentRepository;
    private EnrollmentRepository enrollmentRepository;
    private PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository,
                          EnrollmentRepository enrollmentRepository,
                          PaymentMapper paymentMapper) {

        this.paymentRepository = paymentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentMapper = paymentMapper;
    }

    public PaymentResponse createPayment(CreatePaymentRequest request) {

        if (request.getEnrollmentId() == null ||
                request.getEnrollmentId().isEmpty()) {

            throw new IllegalArgumentException("Enrollment ID is required");
        }

        if (request.getAmount() == null ||
                request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }

        if (request.getPaymentMethod() == null) {
            throw new IllegalArgumentException(
                    "Payment method is required");
        }

        Enrollment enrollment = enrollmentRepository
                .findById(request.getEnrollmentId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Enrollment not found"));

        Payment payment = paymentMapper.toPayment(request);

        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    public PaymentResponse findPaymentById(String id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Payment not found"));

        return paymentMapper.toResponse(payment);
    }

    public Map<String, PaymentResponse> findAllPayments() {

        Map<String, Payment> payments =
                paymentRepository.findAll();

        Map<String, PaymentResponse> responses =
                new HashMap<>();

        for (Map.Entry<String, Payment> entry : payments.entrySet()) {

            responses.put(
                    entry.getKey(),
                    paymentMapper.toResponse(entry.getValue())
            );
        }

        return responses;
    }
}