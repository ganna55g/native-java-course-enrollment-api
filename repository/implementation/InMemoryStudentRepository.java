package com.coursemanagement.repository.implementation;

import com.coursemanagement.model.Student;
import com.coursemanagement.repository.interfaces.StudentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryStudentRepository implements StudentRepository {

    private Map<String, Student> students = new HashMap<>();

    private int nextId = 1;

    @Override
    public Student save(Student student) {
        if (student.getId() == null || student.getId().isEmpty()) {
            student.setId("S" + nextId);
            nextId++;
        }

        students.put(student.getId(), student);

        return student;
    }

    @Override
    public Optional<Student> findById(String id) {
        Student student = students.get(id);
        return Optional.ofNullable(student);
    }

    @Override
    public Optional<Student> findByEmail(String email) {

        for (Student student : students.values()) {

            if (student.getEmail().equals(email)) {
                return Optional.of(student);
            }
        }

        return Optional.empty();
    }

    @Override
    public Map<String, Student> findAll() {
        return students;
    }

    @Override
    public boolean existsByEmail(String email) {

        for (Student student : students.values()) {

            if (student.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteById(String id) {
        students.remove(id);
    }
}