package com.coursemanagement.http.handler;

import com.coursemanagement.Services.EnrollmentService;
import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class EnrollmentHandler implements HttpHandler {

    private EnrollmentService enrollmentService;

    public EnrollmentHandler(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);

        if (!method.equals("POST")) {

            String json = """
                    {
                      "error": "Method Not Allowed"
                    }
                    """;

            HttpUtil.sendJsonResponse(exchange, 405, json);

            return;
        }

        String json = """
                {
                  "message": "POST /api/enrollments will be implemented"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }
}