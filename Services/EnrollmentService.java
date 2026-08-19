package com.coursemanagement.Services;

import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.mapper.EnrollmentMapper;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.repository.interfaces.CourseRepository;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.StudentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             EnrollmentMapper enrollmentMapper) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request) {

        if (request.getStudentId() == null || request.getStudentId().isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }

        if (request.getCourseId() == null || request.getCourseId().isEmpty()) {
            throw new IllegalArgumentException("Course ID is required");
        }

        if (!studentRepository.findById(request.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("Student not found");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.getStudentId(),
                request.getCourseId())) {

            throw new IllegalArgumentException(
                    "Student is already enrolled in this course");
        }

        if (course.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No available seats");
        }

        Enrollment enrollment = enrollmentMapper.toEnrollment(request);

        enrollment.setOriginalPrice(course.getPrice());
        enrollment.setDiscountAmount(BigDecimal.ZERO);
        enrollment.setFinalPrice(course.getPrice());
        enrollment.setEnrollmentDate(LocalDateTime.now());

        course.setAvailableSeats(course.getAvailableSeats() - 1);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        courseRepository.save(course);

        return enrollmentMapper.toResponse(savedEnrollment);
    }

    public EnrollmentResponse findEnrollmentById(String id) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Enrollment not found"));

        return enrollmentMapper.toResponse(enrollment);
    }

    public Map<String, EnrollmentResponse> findAllEnrollments() {

        Map<String, Enrollment> enrollments =
                enrollmentRepository.findAll();

        Map<String, EnrollmentResponse> responses = new HashMap<>();

        for (Map.Entry<String, Enrollment> entry : enrollments.entrySet()) {

            responses.put(
                    entry.getKey(),
                    enrollmentMapper.toResponse(entry.getValue())
            );
        }

        return responses;
    }

    public Map<String, EnrollmentResponse> findEnrollmentsByStudentId(
            String studentId) {

        Map<String, Enrollment> enrollments =
                enrollmentRepository.findAll();

        Map<String, EnrollmentResponse> responses =
                new HashMap<>();

        for (Map.Entry<String, Enrollment> entry : enrollments.entrySet()) {

            Enrollment enrollment = entry.getValue();

            if (enrollment.getStudentId().equals(studentId)) {

                responses.put(
                        entry.getKey(),
                        enrollmentMapper.toResponse(enrollment)
                );
            }
        }

        return responses;
    }
}