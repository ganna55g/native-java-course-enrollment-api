package com.coursemanagement.repository.interfaces;

import com.coursemanagement.model.Payment;

import java.util.Map;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    Optional<Payment> findByEnrollmentId(String enrollmentId);

    Map<String, Payment> findAll();

}