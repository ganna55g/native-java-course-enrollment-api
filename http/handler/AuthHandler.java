package com.coursemanagement.http.handler;

import com.coursemanagement.Services.AuthenticationService;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthHandler implements HttpHandler {

    private AuthenticationService authenticationService;

    public AuthHandler(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);
        String path = HttpUtil.getPath(exchange);

        if (!path.equals("/api/auth/login")) {

            JsonUtil.sendError(
                    exchange,
                    404,
                    "Endpoint not found"
            );

            return;
        }

        if (!method.equals("POST")) {

            JsonUtil.sendError(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }

        try {

            String body = HttpUtil.readBody(exchange);

            if (!JsonParser.hasField(body, "email")
                    || !JsonParser.hasField(body, "password")) {

                JsonUtil.sendError(
                        exchange,
                        400,
                        "Email and password are required"
                );

                return;
            }

            String email =
                    JsonParser.readString(body, "email");

            String password =
                    JsonParser.readString(body, "password");

            String token =
                    authenticationService.login(
                            email,
                            password
                    );

            String role =
                    authenticationService.getRole(token);

            String json = JsonUtil.loginResponseToJson(
                    token,
                    role
            );

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

        } catch (com.coursemanagement.exception.AuthenticationException e) {

            JsonUtil.sendError(
                    exchange,
                    401,
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
}