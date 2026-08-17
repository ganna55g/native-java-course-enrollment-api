package com.coursemanagement.repository.implementation;

import com.coursemanagement.model.Payment;
import com.coursemanagement.repository.interfaces.PaymentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPaymentRepository implements PaymentRepository {

    private Map<String, Payment> payments = new HashMap<>();

    private int nextId = 1;

    @Override
    public Payment save(Payment payment) {

        if (payment.getId() == null || payment.getId().isEmpty()) {
            payment.setId("P" + nextId);
            nextId++;
        }

        payments.put(payment.getId(), payment);

        return payment;
    }

    @Override
    public Optional<Payment> findById(String id) {

        Payment payment = payments.get(id);

        return Optional.ofNullable(payment);
    }

    @Override
    public Optional<Payment> findByEnrollmentId(String enrollmentId) {

        for (Payment payment : payments.values()) {

            if (payment.getEnrollmentId().equals(enrollmentId)) {
                return Optional.of(payment);
            }
        }

        return Optional.empty();
    }

    @Override
    public Map<String, Payment> findAll() {
        return payments;
    }
}