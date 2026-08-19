package com.coursemanagement.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpUtil {

    public static String getMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    public static String getPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    public static String getHeader(HttpExchange exchange, String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    public static String readBody(HttpExchange exchange) throws IOException {

        return new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    public static void sendJsonResponse(
            HttpExchange exchange,
            int statusCode,
            String json
    ) throws IOException {

        exchange.getResponseHeaders()
                .set("Content-Type", "application/json");

        byte[] responseBytes =
                json.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        exchange.getResponseBody().write(responseBytes);
        exchange.getResponseBody().close();
    }

    public static void sendNoBodyResponse(
            HttpExchange exchange,
            int statusCode
    ) throws IOException {

        exchange.sendResponseHeaders(statusCode, -1);
        exchange.close();
    }
}