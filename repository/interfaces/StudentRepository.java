package com.coursemanagement.repository.interfaces;

import com.coursemanagement.model.Student;

import java.util.Map;
import java.util.Optional;

public interface StudentRepository {

    Student save(Student student);

    Optional<Student> findById(String id);

    Optional<Student> findByEmail(String email);

    Map<String, Student> findAll();

    boolean existsByEmail(String email);

    void deleteById(String id);

}