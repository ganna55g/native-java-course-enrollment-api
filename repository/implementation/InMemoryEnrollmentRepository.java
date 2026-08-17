package com.coursemanagement.repository.implementation;

import com.coursemanagement.model.Enrollment;
import com.coursemanagement.repository.interfaces.EnrollmentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

public class InMemoryEnrollmentRepository implements EnrollmentRepository {

    private Map<String, Enrollment> enrollments = new HashMap<>();

    private int nextId = 1;

    @Override
    public Enrollment save(Enrollment enrollment) {

        if (enrollment.getId() == null || enrollment.getId().isEmpty()) {
            enrollment.setId("E" + nextId);
            nextId++;
        }

        enrollments.put(enrollment.getId(), enrollment);

        return enrollment;
    }

    @Override
    public Optional<Enrollment> findById(String id) {

        Enrollment enrollment = enrollments.get(id);

        return Optional.ofNullable(enrollment);
    }

    @Override
    public Map<String, Enrollment> findAll() {
        return enrollments;
    }

    @Override
    public List<Enrollment> findByStudentId(String studentId) {

        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudentId().equals(studentId)) {
                result.add(enrollment);
            }
        }

        return result;
    }

    @Override
    public boolean existsByStudentIdAndCourseId(String studentId, String courseId) {

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudentId().equals(studentId)
                    && enrollment.getCourseId().equals(courseId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteById(String id) {
        enrollments.remove(id);
    }
}