package com.coursemanagement.validator;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.CourseStatus;
import com.coursemanagement.repository.interfaces.CourseRepository;

public class CourseActiveValidator implements EnrollmentValidator {

    private CourseRepository courseRepository;
    private EnrollmentValidator next;

    public CourseActiveValidator(
            CourseRepository courseRepository) {

        this.courseRepository = courseRepository;
    }

    @Override
    public void setNext(EnrollmentValidator next) {
        this.next = next;
    }

    @Override
    public void validate(CreateEnrollmentRequest request) {

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Course not found"
                        ));

        if (course.getStatus() != CourseStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Course is not active"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}