package com.coursemanagement.repository.interfaces;

import com.coursemanagement.model.Course;

import java.util.Map;
import java.util.Optional;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findById(String id);

    Map<String, Course> findAll();

    boolean existsById(String id);

    void deleteById(String id);

}