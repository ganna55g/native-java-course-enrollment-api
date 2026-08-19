package com.coursemanagement.http.handler;

import com.coursemanagement.Services.PaymentService;
import com.coursemanagement.http.HttpUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class PaymentHandler implements HttpHandler {

    private PaymentService paymentService;

    public PaymentHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
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
                  "message": "POST /api/payments will be implemented"
                }
                """;

        HttpUtil.sendJsonResponse(exchange, 200, json);
    }
}