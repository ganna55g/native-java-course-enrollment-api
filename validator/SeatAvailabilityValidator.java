package com.coursemanagement.validator;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.model.Course;
import com.coursemanagement.repository.interfaces.CourseRepository;

public class SeatAvailabilityValidator implements EnrollmentValidator {

    private CourseRepository courseRepository;
    private EnrollmentValidator next;

    public SeatAvailabilityValidator(
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

        if (course.getAvailableSeats() <= 0) {

            throw new IllegalArgumentException(
                    "No available seats"
            );
        }

        if (next != null) {
            next.validate(request);
        }
    }
}