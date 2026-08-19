package com.coursemanagement.http.handler;

import com.coursemanagement.Services.AuthenticationService;
import com.coursemanagement.Services.EnrollmentService;
import com.coursemanagement.Services.PaymentService;
import com.coursemanagement.auth.AuthenticatedUser;
import com.coursemanagement.dto.request.CreatePaymentRequest;
import com.coursemanagement.dto.response.EnrollmentResponse;
import com.coursemanagement.dto.response.PaymentResponse;
import com.coursemanagement.exception.AuthenticationException;
import com.coursemanagement.exception.ForbiddenException;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.coursemanagement.model.PaymentMethod;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.math.BigDecimal;

public class PaymentHandler implements HttpHandler {

    private PaymentService paymentService;
    private EnrollmentService enrollmentService;
    private AuthenticationService authenticationService;

    public PaymentHandler(
            PaymentService paymentService,
            EnrollmentService enrollmentService,
            AuthenticationService authenticationService) {

        this.paymentService = paymentService;
        this.enrollmentService = enrollmentService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);

        try {

            if (!method.equals("POST")) {

                JsonUtil.sendError(
                        exchange,
                        405,
                        "Method Not Allowed"
                );

                return;
            }

            handlePost(exchange);

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

        if (!JsonParser.hasField(body, "enrollmentId")
                || !JsonParser.hasField(body, "amount")
                || !JsonParser.hasField(body, "paymentMethod")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "Enrollment ID, amount and payment method are required"
            );

            return;
        }

        String enrollmentId =
                JsonParser.readString(body, "enrollmentId");

        BigDecimal amount =
                JsonParser.readDecimal(body, "amount");

        PaymentMethod paymentMethod =
                JsonParser.readEnum(
                        body,
                        "paymentMethod",
                        PaymentMethod.class
                );

        String transactionReference = null;

        if (JsonParser.hasField(body, "transactionReference")) {

            transactionReference =
                    JsonParser.readString(
                            body,
                            "transactionReference"
                    );
        }

        EnrollmentResponse enrollment =
                enrollmentService.findEnrollmentById(enrollmentId);

        if (!user.getUserId().equals(enrollment.getStudentId())) {

            throw new ForbiddenException(
                    "You can only pay for your own enrollment"
            );
        }

        CreatePaymentRequest request =
                new CreatePaymentRequest(
                        enrollmentId,
                        amount,
                        paymentMethod,
                        transactionReference
                );

        PaymentResponse payment =
                paymentService.createPayment(request);

        String json =
                JsonUtil.paymentToJson(payment);

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                json
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