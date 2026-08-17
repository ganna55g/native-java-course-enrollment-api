package com.coursemanagement.mapper;

import com.coursemanagement.dto.request.CreateCourseRequest;
import com.coursemanagement.dto.request.UpdateCourseRequest;
import com.coursemanagement.dto.response.CourseResponse;
import com.coursemanagement.model.Course;

public class CourseMapper {

    public Course toCourse(CreateCourseRequest request) {

        Course course = new Course();

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());

        return course;
    }

    public Course updateCourse(Course course, UpdateCourseRequest request) {

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());

        return course;
    }

    public CourseResponse toResponse(Course course) {

        CourseResponse response = new CourseResponse();

        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setPrice(course.getPrice());
        response.setCapacity(course.getCapacity());
        response.setAvailableSeats(course.getAvailableSeats());
        response.setStatus(course.getStatus());
        response.setCreatedAt(course.getCreatedAt());
        response.setUpdatedAt(course.getUpdatedAt());

        return response;
    }
}