package com.coursemanagement.http.json;

import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.dto.response.ErrorResponse;
import com.coursemanagement.dto.response.FieldErrorResponse;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    public static String studentToJson(StudentResponse student) {

        return "{"
                + "\"id\":\"" + escape(student.getId()) + "\","
                + "\"fullName\":\"" + escape(student.getFullName()) + "\","
                + "\"email\":\"" + escape(student.getEmail()) + "\","
                + "\"role\":\"" + student.getRole() + "\","
                + "\"active\":" + student.isActive() + ","
                + "\"createdAt\":\"" + student.getCreatedAt() + "\""
                + "}";
    }

    public static String courseToJson(CourseResponse course) {

        return "{"
                + "\"id\":\"" + escape(course.getId()) + "\","
                + "\"title\":\"" + escape(course.getTitle()) + "\","
                + "\"description\":\"" + escape(course.getDescription()) + "\","
                + "\"price\":" + course.getPrice() + ","
                + "\"capacity\":" + course.getCapacity() + ","
                + "\"availableSeats\":" + course.getAvailableSeats() + ","
                + "\"status\":\"" + course.getStatus() + "\","
                + "\"createdAt\":\"" + course.getCreatedAt() + "\","
                + "\"updatedAt\":\"" + course.getUpdatedAt() + "\""
                + "}";
    }

    public static String enrollmentToJson(
            EnrollmentResponse enrollment) {

        return "{"
                + "\"id\":\"" + escape(enrollment.getId()) + "\","
                + "\"studentId\":\"" + escape(enrollment.getStudentId()) + "\","
                + "\"courseId\":\"" + escape(enrollment.getCourseId()) + "\","
                + "\"originalPrice\":" + enrollment.getOriginalPrice() + ","
                + "\"discountAmount\":" + enrollment.getDiscountAmount() + ","
                + "\"finalPrice\":" + enrollment.getFinalPrice() + ","
                + "\"status\":\"" + enrollment.getStatus() + "\","
                + "\"enrollmentDate\":\"" + enrollment.getEnrollmentDate() + "\""
                + "}";
    }

    public static String paymentToJson(
            PaymentResponse payment) {

        return "{"
                + "\"id\":\"" + escape(payment.getId()) + "\","
                + "\"enrollmentId\":\""
                + escape(payment.getEnrollmentId()) + "\","
                + "\"amount\":" + payment.getAmount() + ","
                + "\"paymentMethod\":\""
                + payment.getPaymentMethod() + "\","
                + "\"paymentStatus\":\""
                + payment.getPaymentStatus() + "\","
                + "\"transactionReference\":\""
                + escape(payment.getTransactionReference()) + "\","
                + "\"paymentDate\":\""
                + payment.getPaymentDate() + "\""
                + "}";
    }

    public static String studentsToJson(
            List<StudentResponse> students) {

        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < students.size(); i++) {

            json.append(studentToJson(students.get(i)));

            if (i < students.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String coursesToJson(
            List<CourseResponse> courses) {

        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < courses.size(); i++) {

            json.append(courseToJson(courses.get(i)));

            if (i < courses.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String enrollmentsToJson(
            List<EnrollmentResponse> enrollments) {

        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < enrollments.size(); i++) {

            json.append(
                    enrollmentToJson(enrollments.get(i))
            );

            if (i < enrollments.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    public static String errorToJson(String message) {

        ErrorResponse error =
                new ErrorResponse(message);

        return "{"
                + "\"message\":\""
                + escape(error.getMessage())
                + "\""
                + "}";
    }

    public static String fieldErrorToJson(
            String field,
            String message) {

        FieldErrorResponse error =
                new FieldErrorResponse(field, message);

        return "{"
                + "\"field\":\""
                + escape(error.getField()) + "\","
                + "\"message\":\""
                + escape(error.getMessage())
                + "\""
                + "}";
    }

    public static String loginResponseToJson(
            String token,
            String role) {

        return "{"
                + "\"accessToken\":\"" + escape(token) + "\","
                + "\"tokenType\":\"Bearer\","
                + "\"role\":\"" + escape(role) + "\""
                + "}";
    }

    public static void sendError(
            HttpExchange exchange,
            int statusCode,
            String message) throws IOException {

        String json = errorToJson(message);

        HttpUtil.sendJsonResponse(
                exchange,
                statusCode,
                json
        );
    }

    private static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}