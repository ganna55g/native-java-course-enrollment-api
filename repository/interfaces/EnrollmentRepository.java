package com.coursemanagement.repository.interfaces;

import com.coursemanagement.model.Enrollment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EnrollmentRepository {

    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(String id);

    Map<String, Enrollment> findAll();

    List<Enrollment> findByStudentId(String studentId);

    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    void deleteById(String id);

}