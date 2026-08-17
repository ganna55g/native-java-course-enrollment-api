package com.coursemanagement.mapper;

import com.coursemanagement.dto.request.RegisterStudentRequest;
import com.coursemanagement.dto.response.StudentResponse;
import com.coursemanagement.model.Student;

public class StudentMapper {

    public Student toStudent(RegisterStudentRequest request) {

        Student student = new Student();

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setPassword(request.getPassword());

        return student;
    }

    public StudentResponse toResponse(Student student) {

        StudentResponse response = new StudentResponse();

        response.setId(student.getId());
        response.setFullName(student.getFullName());
        response.setEmail(student.getEmail());
        response.setRole(student.getRole());
        response.setActive(student.isActive());
        response.setCreatedAt(student.getCreatedAt());

        return response;
    }
}