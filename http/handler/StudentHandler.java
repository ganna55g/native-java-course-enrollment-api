package com.coursemanagement.http.handler;

import com.coursemanagement.Services.StudentService;
import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class StudentHandler implements HttpHandler {

    private StudentService studentService;

    public StudentHandler(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);
        String path = HttpUtil.getPath(exchange);

        if (method.equals("POST")) {

            handlePost(exchange);

        } else if (method.equals("GET")) {

            handleGet(exchange, path);

        } else {

            String json = """
                    {
                      "error": "Method Not Allowed"
                    }
                    """;

            HttpUtil.sendJsonResponse(exchange, 405, json);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {

        String json = """
                {
                  "message": "POST /api/students will be implemented with JSON"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }

    private void handleGet(HttpExchange exchange, String path)
            throws IOException {

        String json = """
                {
                  "message": "GET /api/students will be implemented"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }
}