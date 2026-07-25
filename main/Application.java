package com.coursemanagement.main;

import com.coursemanagement.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Application {

    public static void main(String[] args) {

        System.out.println("Course Enrollment Management System");
        System.out.println("Application started successfully");

        Student student = new Student(
                "S001",
                "Ahmed Ali",
                "ahmed@gmail.com",
                "123456",
                Role.STUDENT,
                true,
                LocalDateTime.now()
        );

        Course course = new Course(
                "C001",
                "Java",
                "Java Basics",
                new BigDecimal("1500"),
                20,
                20,
                CourseStatus.PUBLISHED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Enrollment enrollment = new Enrollment(
                "E001",
                "S001",
                "C001",
                new BigDecimal("1500"),
                new BigDecimal("0"),
                new BigDecimal("1500"),
                EnrollmentStatus.ACTIVE,
                LocalDateTime.now()
        );

        Payment payment = new Payment(
                "P001",
                "E001",
                new BigDecimal("1500"),
                PaymentMethod.CREDIT_CARD,
                PaymentStatus.COMPLETED,
                "TXN001",
                LocalDateTime.now()
        );

        AuditLog auditLog = new AuditLog(
                "A001",
                "Create Student",
                "Student",
                "S001",
                "Student registered successfully",
                LocalDateTime.now()
        );

        System.out.println(student);
        System.out.println(course);
        System.out.println(enrollment);
        System.out.println(payment);
        System.out.println(auditLog);
    }
}