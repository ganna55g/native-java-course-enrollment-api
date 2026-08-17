package com.coursemanagement.Services;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.mapper.CourseMapper;
import com.coursemanagement.model.Course;
import com.coursemanagement.repository.interfaces.CourseRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
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

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (request.getPrice() == null ||
                request.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (request.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

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

    public CourseResponse replaceCourse(String id,
                                        UpdateCourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found"));

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (request.getDescription() == null ||
                request.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (request.getPrice() == null ||
                request.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (request.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }

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
}