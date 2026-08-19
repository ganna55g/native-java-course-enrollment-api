package com.coursemanagement.http.handler;

import com.coursemanagement.Services.CourseService;
import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.coursemanagement.model.CourseStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseHandler implements HttpHandler {

    private CourseService courseService;

    public CourseHandler(CourseService courseService) {
        this.courseService = courseService;
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

        if (!JsonParser.hasField(body, "title")
                || !JsonParser.hasField(body, "description")
                || !JsonParser.hasField(body, "price")
                || !JsonParser.hasField(body, "capacity")
                || !JsonParser.hasField(body, "status")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "Missing required field"
            );

            return;
        }

        String title =
                JsonParser.readString(body, "title");

        String description =
                JsonParser.readString(body, "description");

        BigDecimal price =
                JsonParser.readDecimal(body, "price");

        int capacity =
                JsonParser.readInteger(body, "capacity");

        CourseStatus status =
                JsonParser.readEnum(
                        body,
                        "status",
                        CourseStatus.class
                );

        CreateCourseRequest request =
                new CreateCourseRequest(
                        title,
                        description,
                        price,
                        capacity,
                        status
                );

        CourseResponse course =
                courseService.createCourse(request);

        String json =
                JsonUtil.courseToJson(course);

        exchange.getResponseHeaders()
                .set(
                        "Location",
                        "/api/courses/" + course.getId()
                );

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

        if (path.equals("/api/courses")) {

            Map<String, CourseResponse> courses =
                    courseService.findAllCourses();

            List<CourseResponse> courseList =
                    new ArrayList<>(courses.values());

            String json =
                    JsonUtil.coursesToJson(courseList);

            HttpUtil.sendJsonResponse(
                    exchange,
                    200,
                    json
            );

            return;
        }

        if (path.startsWith("/api/courses/")) {

            String id =
                    path.substring("/api/courses/".length());

            if (id.isEmpty()) {

                JsonUtil.sendError(
                        exchange,
                        400,
                        "Course ID is required"
                );

                return;
            }

            CourseResponse course =
                    courseService.findCourseById(id);

            String json =
                    JsonUtil.courseToJson(course);

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
                "Course not found"
        );
    }
}