package com.coursemanagement.http;

import com.coursemanagement.Services.AuthenticationService;
import com.coursemanagement.http.handler.AuthHandler;
import com.coursemanagement.http.handler.CourseHandler;
import com.coursemanagement.http.handler.EnrollmentHandler;
import com.coursemanagement.http.handler.HealthHandler;
import com.coursemanagement.http.handler.PaymentHandler;
import com.coursemanagement.http.handler.StudentHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {

    private HttpServer server;

    private HealthHandler healthHandler;
    private StudentHandler studentHandler;
    private CourseHandler courseHandler;
    private AuthHandler authHandler;
    private EnrollmentHandler enrollmentHandler;
    private PaymentHandler paymentHandler;

    public HttpServerManager(
            HealthHandler healthHandler,
            StudentHandler studentHandler,
            CourseHandler courseHandler,
            AuthHandler authHandler,
            EnrollmentHandler enrollmentHandler,
            PaymentHandler paymentHandler) {

        this.healthHandler = healthHandler;
        this.studentHandler = studentHandler;
        this.courseHandler = courseHandler;
        this.authHandler = authHandler;
        this.enrollmentHandler = enrollmentHandler;
        this.paymentHandler = paymentHandler;
    }

    public void start() throws IOException {

        server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        server.createContext("/api/health", healthHandler);
        server.createContext("/api/students", studentHandler);
        server.createContext("/api/courses", courseHandler);
        server.createContext("/api/auth", authHandler);
        server.createContext("/api/enrollments", enrollmentHandler);
        server.createContext("/api/payments", paymentHandler);

        server.start();

        System.out.println(
                "Course Enrollment API started on http://localhost:8080"
        );
    }
}