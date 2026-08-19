package com.coursemanagement.Services;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.mapper.CourseMapper;
import com.coursemanagement.model.Course;
import com.coursemanagement.repository.interfaces.CourseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository,
                         CourseMapper courseMapper) {

        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public CourseResponse createCourse(CreateCourseRequest request) {

        validateCourseRequest(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getCapacity(),
                request.getStatus()
        );

        Course course = courseMapper.toCourse(request);

        course.setAvailableSeats(course.getCapacity());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse findCourseById(String id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        return courseMapper.toResponse(course);
    }

    public Map<String, CourseResponse> findAllCourses() {

        Map<String, Course> courses = courseRepository.findAll();

        Map<String, CourseResponse> responses = new HashMap<>();

        for (Map.Entry<String, Course> entry : courses.entrySet()) {

            responses.put(
                    entry.getKey(),
                    courseMapper.toResponse(entry.getValue())
            );
        }

        return responses;
    }

    public CourseResponse replaceCourse(
            String id,
            UpdateCourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        validateCourseRequest(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getCapacity(),
                request.getStatus()
        );

        courseMapper.updateCourse(course, request);

        if (course.getAvailableSeats() > course.getCapacity()) {
            course.setAvailableSeats(course.getCapacity());
        }

        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toResponse(updatedCourse);
    }

    public CourseResponse updateCourseStatus(
            String id,
            UpdateCourseStatusRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status is required");
        }

        course.setStatus(request.getStatus());
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toResponse(updatedCourse);
    }

    public void deleteCourse(String id) {

        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found");
        }

        courseRepository.deleteById(id);
    }

    public List<CourseResponse> filterCourses(
            String status,
            String title,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sort) {

        if (minPrice != null
                && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "minPrice cannot be greater than maxPrice"
            );
        }

        if (status != null && !status.isEmpty()) {

            try {
                com.coursemanagement.model.CourseStatus.valueOf(
                        status.toUpperCase()
                );
            } catch (IllegalArgumentException e) {

                throw new IllegalArgumentException(
                        "Invalid status"
                );
            }
        }

        if (sort != null && !sort.isEmpty()) {

            if (!sort.equalsIgnoreCase("price,asc")
                    && !sort.equalsIgnoreCase("price,desc")) {

                throw new IllegalArgumentException(
                        "Invalid sort parameter"
                );
            }
        }

        Map<String, Course> courses =
                courseRepository.findAll();

        List<Course> filteredCourses =
                new ArrayList<>(courses.values());

        if (status != null && !status.isEmpty()) {

            filteredCourses.removeIf(course ->
                    !course.getStatus()
                            .name()
                            .equalsIgnoreCase(status)
            );
        }

        if (title != null && !title.isEmpty()) {

            filteredCourses.removeIf(course ->
                    !course.getTitle()
                            .toLowerCase()
                            .contains(title.toLowerCase())
            );
        }

        if (minPrice != null) {

            filteredCourses.removeIf(course ->
                    course.getPrice()
                            .compareTo(minPrice) < 0
            );
        }

        if (maxPrice != null) {

            filteredCourses.removeIf(course ->
                    course.getPrice()
                            .compareTo(maxPrice) > 0
            );
        }

        if (sort != null && !sort.isEmpty()) {

            if (sort.equalsIgnoreCase("price,asc")) {

                filteredCourses.sort(
                        Comparator.comparing(Course::getPrice)
                );

            } else {

                filteredCourses.sort(
                        Comparator.comparing(Course::getPrice)
                                .reversed()
                );
            }
        }

        List<CourseResponse> responses =
                new ArrayList<>();

        for (Course course : filteredCourses) {

            responses.add(
                    courseMapper.toResponse(course)
            );
        }

        return responses;
    }

    private void validateCourseRequest(
            String title,
            String description,
            BigDecimal price,
            int capacity,
            com.coursemanagement.model.CourseStatus status) {

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException(
                    "Description is required"
            );
        }

        if (price == null
                || price.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero"
            );
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than zero"
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Status is required"
            );
        }
    }
}