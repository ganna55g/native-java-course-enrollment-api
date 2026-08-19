package com.coursemanagement.http.handler;

import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthHandler implements HttpHandler {

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
                  "message": "Authentication endpoint"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }
}