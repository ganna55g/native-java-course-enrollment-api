package com.coursemanagement.mapper;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.model.Enrollment;

public class EnrollmentMapper {

    public Enrollment toEnrollment(CreateEnrollmentRequest request) {

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(request.getStudentId());
        enrollment.setCourseId(request.getCourseId());

        return enrollment;
    }

    public EnrollmentResponse toResponse(Enrollment enrollment) {

        EnrollmentResponse response = new EnrollmentResponse();

        response.setId(enrollment.getId());
        response.setStudentId(enrollment.getStudentId());
        response.setCourseId(enrollment.getCourseId());
        response.setOriginalPrice(enrollment.getOriginalPrice());
        response.setDiscountAmount(enrollment.getDiscountAmount());
        response.setFinalPrice(enrollment.getFinalPrice());
        response.setStatus(enrollment.getStatus());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());

        return response;
    }
}