package com.coursemanagement.repository.implementation;

import com.coursemanagement.model.Course;
import com.coursemanagement.repository.interfaces.CourseRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCourseRepository implements CourseRepository {

    private Map<String, Course> courses = new HashMap<>();

    private int nextId = 1;

    @Override
    public Course save(Course course) {

        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId("C" + nextId);
            nextId++;
        }

        courses.put(course.getId(), course);

        return course;
    }

    @Override
    public Optional<Course> findById(String id) {
        Course course = courses.get(id);
        return Optional.ofNullable(course);
    }

    @Override
    public Map<String, Course> findAll() {
        return courses;
    }

    @Override
    public boolean existsById(String id) {
        return courses.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        courses.remove(id);
    }
}