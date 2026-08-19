package com.coursemanagement.http.handler;

import com.coursemanagement.Services.AuthenticationService;
import com.coursemanagement.Services.EnrollmentService;
import com.coursemanagement.auth.AuthenticatedUser;
import com.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.exception.AuthenticationException;
import com.coursemanagement.exception.ForbiddenException;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.coursemanagement.model.Role;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrollmentHandler implements HttpHandler {

    private EnrollmentService enrollmentService;
    private AuthenticationService authenticationService;

    public EnrollmentHandler(
            EnrollmentService enrollmentService,
            AuthenticationService authenticationService) {

        this.enrollmentService = enrollmentService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);
        String path = HttpUtil.getPath(exchange);

        try {

            if (method.equals("POST")) {

                handlePost(exchange);

            } else if (method.equals("GET")) {

                handleGet(exchange, path);

            } else {

                JsonUtil.sendError(
                        exchange,
                        405,
                        "Method Not Allowed"
                );
            }

        } catch (AuthenticationException e) {

            JsonUtil.sendError(
                    exchange,
                    401,
                    e.getMessage()
            );

        } catch (ForbiddenException e) {

            JsonUtil.sendError(
                    exchange,
                    403,
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    e.getMessage()
            );
        }
    }

    private void handlePost(
            HttpExchange exchange) throws IOException {

        AuthenticatedUser user =
                getAuthenticatedUser(exchange);

        String body =
                HttpUtil.readBody(exchange);

        if (!JsonParser.hasField(body, "studentId")
                || !JsonParser.hasField(body, "courseId")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "Student ID and Course ID are required"
            );

            return;
        }

        String studentId =
                JsonParser.readString(body, "studentId");

        String courseId =
                JsonParser.readString(body, "courseId");

        if (user.getRole() == Role.STUDENT
                && !user.getUserId().equals(studentId)) {

            throw new ForbiddenException(
                    "You can only create enrollments for yourself"
            );
        }

        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(
                        studentId,
                        courseId
                );

        EnrollmentResponse enrollment =
                enrollmentService.createEnrollment(request);

        String json =
                JsonUtil.enrollmentToJson(enrollment);

        HttpUtil.sendJsonResponse(
                exchange,
                201,
                json
        );
    }

    private void handleGet(
            HttpExchange exchange,
            String path) throws IOException {

        AuthenticatedUser user =
                getAuthenticatedUser(exchange);

        if (path.equals("/api/enrollments")) {

            if (user.getRole() != Role.ADMIN) {

                throw new ForbiddenException(
                        "You are not allowed to view all enrollments"
                );
            }

            Map<String, EnrollmentResponse> enrollments =
                    enrollmentService.findAllEnrollments();

            List<EnrollmentResponse> enrollmentList =
                    new ArrayList<>(enrollments.values());

            String json =
                    JsonUtil.enrollmentsToJson(enrollmentList);

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

            return;
        }

        if (path.startsWith("/api/enrollments/")) {

            String id =
                    path.substring(
                            "/api/enrollments/".length()
                    );

            if (id.isEmpty()) {

                JsonUtil.sendError(
                        exchange,
                        400,
                        "Enrollment ID is required"
                );

                return;
            }

            EnrollmentResponse enrollment =
                    enrollmentService.findEnrollmentById(id);

            if (user.getRole() != Role.ADMIN
                    && !user.getUserId()
                    .equals(enrollment.getStudentId())) {

                throw new ForbiddenException(
                        "You are not allowed to view this enrollment"
                );
            }

            String json =
                    JsonUtil.enrollmentToJson(enrollment);

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

            return;
        }

        JsonUtil.sendError(
                exchange,
                404,
                "Enrollment not found"
        );
    }

    private AuthenticatedUser getAuthenticatedUser(
            HttpExchange exchange) {

        String header =
                HttpUtil.getHeader(
                        exchange,
                        "Authorization"
                );

        if (header == null
                || !header.startsWith("Bearer ")) {

            throw new AuthenticationException(
                    "Unauthorized"
            );
        }

        String token =
                header.substring(7);

        return authenticationService.authenticate(token);
    }
}