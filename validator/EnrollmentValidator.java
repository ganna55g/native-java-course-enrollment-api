package com.coursemanagement.validator;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;

public interface EnrollmentValidator {

    void setNext(EnrollmentValidator next);

    void validate(CreateEnrollmentRequest request);
}