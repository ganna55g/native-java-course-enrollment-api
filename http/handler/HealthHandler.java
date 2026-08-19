package com.coursemanagement.http.handler;

import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HealthHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);

        if (!method.equals("GET")) {

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
                  "status": "UP",
                  "application": "Course Enrollment API"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }
}