package com.coursemanagement.http.handler;

import com.coursemanagement.Services.StudentService;
import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentHandler implements HttpHandler {

    private StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
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

        } catch (IllegalArgumentException e) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    e.getMessage()
            );
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {

        String body = HttpUtil.readBody(exchange);

        if (!JsonParser.hasField(body, "fullName")
                || !JsonParser.hasField(body, "email")
                || !JsonParser.hasField(body, "password")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "Missing required field"
            );

            return;
        }

        String fullName =
                JsonParser.readString(body, "fullName");

        String email =
                JsonParser.readString(body, "email");

        String password =
                JsonParser.readString(body, "password");

        RegisterStudentRequest request =
                new RegisterStudentRequest(
                        fullName,
                        email,
                        password
                );

        StudentResponse student =
                studentService.registerStudent(request);

        String json =
                JsonUtil.studentToJson(student);

        HttpUtil.sendJsonResponse(
                exchange,
                201,
                json
        );
    }

    private void handleGet(
            HttpExchange exchange,
            String path
    ) throws IOException {

        if (path.equals("/api/students")) {

            Map<String, StudentResponse> students =
                    studentService.findAllStudents();

            List<StudentResponse> studentList =
                    new ArrayList<>(students.values());

            String json =
                    JsonUtil.studentsToJson(studentList);

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

            return;
        }

        if (path.startsWith("/api/students/")) {

            String id =
                    path.substring("/api/students/".length());

            if (id.isEmpty()) {

                JsonUtil.sendError(
                        exchange,
                        400,
                        "Student ID is required"
                );

                return;
            }

            StudentResponse student =
                    studentService.findStudentById(id);

            String json =
                    JsonUtil.studentToJson(student);

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
                "Student not found"
        );
    }
}