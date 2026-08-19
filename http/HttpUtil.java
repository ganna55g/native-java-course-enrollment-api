package com.coursemanagement.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static String getMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    public static String getPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    public static String getHeader(
            HttpExchange exchange,
            String name) {

        return exchange.getRequestHeaders().getFirst(name);
    }

    public static void addHeader(
            HttpExchange exchange,
            String name,
            String value) {

        exchange.getResponseHeaders().set(name, value);
    }

    public static String readBody(
            HttpExchange exchange) throws IOException {

        return new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    public static Map<String, String> getQueryParameters(
            HttpExchange exchange) {

        Map<String, String> parameters = new HashMap<>();

        String query = exchange.getRequestURI().getQuery();

        if (query == null || query.isEmpty()) {
            return parameters;
        }

        String[] parts = query.split("&");

        for (String part : parts) {

            String[] keyValue = part.split("=", 2);

            String key = keyValue[0];

            String value = "";

            if (keyValue.length == 2) {
                value = keyValue[1];
            }

            parameters.put(key, value);
        }

        return parameters;
    }

    public static void sendJsonResponse(
            HttpExchange exchange,
            int statusCode,
            String json) throws IOException {

        addHeader(
                exchange,
                "Content-Type",
                "application/json"
        );

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
            int statusCode) throws IOException {

        exchange.sendResponseHeaders(
                statusCode,
                -1
        );

        exchange.close();
    }
}