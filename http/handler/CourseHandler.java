package com.coursemanagement.http.handler;

import com.coursemanagement.Services.AuthenticationService;
import com.coursemanagement.Services.CourseService;
import com.coursemanagement.auth.AuthenticatedUser;
import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.exception.AuthenticationException;
import com.coursemanagement.exception.ForbiddenException;
import com.coursemanagement.http.HttpUtil;
import com.coursemanagement.http.json.JsonParser;
import com.coursemanagement.http.json.JsonUtil;
import com.coursemanagement.model.CourseStatus;
import com.coursemanagement.model.Role;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseHandler implements HttpHandler {

    private CourseService courseService;
    private AuthenticationService authenticationService;

    public CourseHandler(CourseService courseService,
                         AuthenticationService authenticationService) {

        this.courseService = courseService;
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = HttpUtil.getMethod(exchange);
        String path = HttpUtil.getPath(exchange);

        try {

            if (method.equals("GET")) {

                handleGet(exchange, path);

            } else if (method.equals("POST")) {

                handlePost(exchange);

            } else if (method.equals("PUT")) {

                handlePut(exchange, path);

            } else if (method.equals("PATCH")) {

                handlePatch(exchange, path);

            } else if (method.equals("DELETE")) {

                handleDelete(exchange, path);

            } else {

                JsonUtil.sendError(
                        exchange,
                        405,
                        "Method Not Allowed"
                );
            }

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

            if (e.getMessage() != null
                    && e.getMessage().equals("Course not found")) {

                JsonUtil.sendError(
                        exchange,
                        404,
                        e.getMessage()
                );

                return;
            }

            JsonUtil.sendError(
                    exchange,
                    400,
                    e.getMessage()
            );
        }
    }

    private void handleGet(
            HttpExchange exchange,
            String path) throws IOException {

        if (path.equals("/api/courses")) {

            Map<String, String> parameters =
                    HttpUtil.getQueryParameters(exchange);

            String status = parameters.get("status");
            String title = parameters.get("title");

            BigDecimal minPrice = null;
            BigDecimal maxPrice = null;

            if (parameters.containsKey("minPrice")) {

                minPrice = new BigDecimal(
                        parameters.get("minPrice")
                );
            }

            if (parameters.containsKey("maxPrice")) {

                maxPrice = new BigDecimal(
                        parameters.get("maxPrice")
                );
            }

            String sort = parameters.get("sort");

            List<CourseResponse> courses =
                    courseService.filterCourses(
                            status,
                            title,
                            minPrice,
                            maxPrice,
                            sort
                    );

            String json =
                    JsonUtil.coursesToJson(courses);

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

    private void handlePost(
            HttpExchange exchange) throws IOException {

        checkAdmin(exchange);

        String body =
                HttpUtil.readBody(exchange);

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

        HttpUtil.addHeader(
                exchange,
                "Location",
                "/api/courses/" + course.getId()
        );

        String json =
                JsonUtil.courseToJson(course);

        HttpUtil.sendJsonResponse(
                exchange,
                201,
                json
        );
    }

    private void handlePut(
            HttpExchange exchange,
            String path) throws IOException {

        checkAdmin(exchange);

        String id =
                getCourseId(path);

        if (id == null) {

            JsonUtil.sendError(
                    exchange,
                    404,
                    "Course not found"
            );

            return;
        }

        String body =
                HttpUtil.readBody(exchange);

        if (!JsonParser.hasField(body, "title")
                || !JsonParser.hasField(body, "description")
                || !JsonParser.hasField(body, "price")
                || !JsonParser.hasField(body, "capacity")
                || !JsonParser.hasField(body, "status")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "All course fields are required for PUT"
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

        UpdateCourseRequest request =
                new UpdateCourseRequest(
                        title,
                        description,
                        price,
                        capacity,
                        status
                );

        CourseResponse course =
                courseService.replaceCourse(
                        id,
                        request
                );

        String json =
                JsonUtil.courseToJson(course);

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                json
        );
    }

    private void handlePatch(
            HttpExchange exchange,
            String path) throws IOException {

        checkAdmin(exchange);

        if (!path.endsWith("/status")) {

            JsonUtil.sendError(
                    exchange,
                    404,
                    "Course status endpoint not found"
            );

            return;
        }

        String id =
                path.substring(
                        "/api/courses/".length(),
                        path.length() - "/status".length()
                );

        if (id.isEmpty()) {

            JsonUtil.sendError(
                    exchange,
                    404,
                    "Course not found"
            );

            return;
        }

        String body =
                HttpUtil.readBody(exchange);

        if (!JsonParser.hasField(body, "status")) {

            JsonUtil.sendError(
                    exchange,
                    400,
                    "Status is required"
            );

            return;
        }

        CourseStatus status =
                JsonParser.readEnum(
                        body,
                        "status",
                        CourseStatus.class
                );

        UpdateCourseStatusRequest request =
                new UpdateCourseStatusRequest(status);

        CourseResponse course =
                courseService.updateCourseStatus(
                        id,
                        request
                );

        String json =
                JsonUtil.courseToJson(course);

        HttpUtil.sendJsonResponse(
                exchange,
                200,
                json
        );
    }

    private void handleDelete(
            HttpExchange exchange,
            String path) throws IOException {

        checkAdmin(exchange);

        String id =
                getCourseId(path);

        if (id == null) {

            JsonUtil.sendError(
                    exchange,
                    404,
                    "Course not found"
            );

            return;
        }

        courseService.deleteCourse(id);

        HttpUtil.sendNoBodyResponse(
                exchange,
                204
        );
    }

    private void checkAdmin(HttpExchange exchange) {

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

        AuthenticatedUser user =
                authenticationService.authenticate(token);

        if (user.getRole() != Role.ADMIN) {

            throw new ForbiddenException(
                    "You are not allowed to perform this operation"
            );
        }
    }

    private String getCourseId(String path) {

        if (!path.startsWith("/api/courses/")) {

            return null;
        }

        String id =
                path.substring("/api/courses/".length());

        if (id.isEmpty() || id.contains("/")) {

            return null;
        }

        return id;
    }
}