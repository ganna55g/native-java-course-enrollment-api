package com.coursemanagement.validator;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.repository.interfaces.StudentRepository;

public class StudentExistenceValidator implements EnrollmentValidator {

    private StudentRepository studentRepository;
    private EnrollmentValidator next;

    public StudentExistenceValidator(
            StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        if (request.getStudentId() == null
                || request.getStudentId().isEmpty()) {

            throw new IllegalArgumentException(
                    "Student ID is required"
            );
        }

        if (!studentRepository
                .findById(request.getStudentId())
                .isPresent()) {

            throw new IllegalArgumentException(
                    "Student not found"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}