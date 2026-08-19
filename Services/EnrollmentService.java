package com.coursemanagement.Services;

import com.coursemanagement.discount.DiscountStrategy;
import com.coursemanagement.discount.DiscountStrategyFactory;
import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.mapper.EnrollmentMapper;
import com.coursemanagement.model.Course;
import com.coursemanagement.model.Enrollment;
import com.coursemanagement.model.EnrollmentStatus;
import com.coursemanagement.repository.interfaces.CourseRepository;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;
import com.coursemanagement.repository.interfaces.StudentRepository;
import com.coursemanagement.validator.CourseActiveValidator;
import com.coursemanagement.validator.CourseExistenceValidator;
import com.coursemanagement.validator.DuplicateEnrollmentValidator;
import com.coursemanagement.validator.EnrollmentValidator;
import com.coursemanagement.validator.SeatAvailabilityValidator;
import com.coursemanagement.validator.StudentExistenceValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private CourseRepository courseRepository;
    private EnrollmentMapper enrollmentMapper;
    private EnrollmentValidator validatorChain;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentMapper enrollmentMapper) {

        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;

        this.validatorChain = createValidatorChain(
                studentRepository,
                courseRepository,
                enrollmentRepository
        );
    }

    public EnrollmentResponse createEnrollment(
            CreateEnrollmentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Enrollment request is required"
            );
        }

        if (request.getDiscountType() == null) {
            throw new IllegalArgumentException(
                    "Discount type is required"
            );
        }

        validatorChain.validate(request);

        Course course =
                courseRepository.findById(request.getCourseId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Course not found"
                                ));

        DiscountStrategy strategy =
                DiscountStrategyFactory.getStrategy(
                        request.getDiscountType()
                );

        BigDecimal originalPrice =
                course.getPrice();

        BigDecimal discountAmount =
                strategy.calculateDiscount(originalPrice);

        BigDecimal finalPrice =
                originalPrice.subtract(discountAmount);

        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Final price cannot be negative"
            );
        }

        Enrollment enrollment =
                enrollmentMapper.toEnrollment(request);

        enrollment.setOriginalPrice(originalPrice);
        enrollment.setDiscountAmount(discountAmount);
        enrollment.setFinalPrice(finalPrice);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(
                EnrollmentStatus.PENDING_PAYMENT
        );

        course.setAvailableSeats(
                course.getAvailableSeats() - 1
        );

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        courseRepository.save(course);

        return enrollmentMapper.toResponse(
                savedEnrollment
        );
    }

    public EnrollmentResponse findEnrollmentById(
            String id) {

        Enrollment enrollment =
                enrollmentRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Enrollment not found"
                                ));

        return enrollmentMapper.toResponse(
                enrollment
        );
    }

    public Map<String, EnrollmentResponse> findAllEnrollments() {

        Map<String, Enrollment> enrollments =
                enrollmentRepository.findAll();

        Map<String, EnrollmentResponse> responses =
                new HashMap<>();

        for (Map.Entry<String, Enrollment> entry
                : enrollments.entrySet()) {

            responses.put(
                    entry.getKey(),
                    enrollmentMapper.toResponse(
                            entry.getValue()
                    )
            );
        }

        return responses;
    }

    public Map<String, EnrollmentResponse>
    findEnrollmentsByStudentId(String studentId) {

        Map<String, Enrollment> enrollments =
                enrollmentRepository.findAll();

        Map<String, EnrollmentResponse> responses =
                new HashMap<>();

        for (Map.Entry<String, Enrollment> entry
                : enrollments.entrySet()) {

            Enrollment enrollment =
                    entry.getValue();

            if (enrollment.getStudentId()
                    .equals(studentId)) {

                responses.put(
                        entry.getKey(),
                        enrollmentMapper.toResponse(
                                enrollment
                        )
                );
            }
        }

        return responses;
    }

    private EnrollmentValidator createValidatorChain(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository) {

        EnrollmentValidator studentValidator =
                new StudentExistenceValidator(
                        studentRepository
                );

        EnrollmentValidator courseValidator =
                new CourseExistenceValidator(
                        courseRepository
                );

        EnrollmentValidator activeValidator =
                new CourseActiveValidator(
                        courseRepository
                );

        EnrollmentValidator seatValidator =
                new SeatAvailabilityValidator(
                        courseRepository
                );

        EnrollmentValidator duplicateValidator =
                new DuplicateEnrollmentValidator(
                        enrollmentRepository
                );

        studentValidator.setNext(courseValidator);
        courseValidator.setNext(activeValidator);
        activeValidator.setNext(seatValidator);
        seatValidator.setNext(duplicateValidator);

        return studentValidator;
    }

    public void deleteEnrollment(String id) {

        if (!enrollmentRepository.findById(id).isPresent()) {

            throw new IllegalArgumentException(
                    "Enrollment not found"
            );
        }

        enrollmentRepository.deleteById(id);
    }
}