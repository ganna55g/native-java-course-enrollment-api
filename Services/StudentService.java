package com.coursemanagement.Services;

import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.mapper.StudentMapper;
import com.coursemanagement.model.Role;
import com.coursemanagement.model.Student;
import com.coursemanagement.repository.interfaces.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StudentService {

    private StudentRepository studentRepository;
    private StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository,
                          StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentResponse registerStudent(RegisterStudentRequest request) {

        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must contain at least 6 characters");
        }

        Student student = studentMapper.toStudent(request);

        student.setRole(Role.STUDENT);
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());

        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponse(savedStudent);
    }

    public StudentResponse findStudentById(String id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        return studentMapper.toResponse(student);
    }

    public Map<String, StudentResponse> findAllStudents() {

        Map<String, Student> students = studentRepository.findAll();

        Map<String, StudentResponse> responses = new java.util.HashMap<>();

        for (Map.Entry<String, Student> entry : students.entrySet()) {

            responses.put(
                    entry.getKey(),
                    studentMapper.toResponse(entry.getValue())
            );
        }

        return responses;
    }
}