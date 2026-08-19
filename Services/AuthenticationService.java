package com.coursemanagement.Services;

import com.coursemanagement.auth.AuthenticatedUser;
import com.coursemanagement.exception.AuthenticationException;
import com.coursemanagement.model.Student;
import com.coursemanagement.repository.interfaces.StudentRepository;
import com.coursemanagement.repository.interfaces.TokenRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {

    private StudentRepository studentRepository;
    private TokenRepository tokenRepository;

    public AuthenticationService(StudentRepository studentRepository,
                                 TokenRepository tokenRepository) {

        this.studentRepository = studentRepository;
        this.tokenRepository = tokenRepository;
    }

    public String getRole(String token) {

        AuthenticatedUser user = authenticate(token);

        return user.getRole().name();
    }

    public String login(String email, String password) {

        Optional<Student> student =
                studentRepository.findByEmail(email);

        if (student.isEmpty()) {
            throw new AuthenticationException(
                    "Invalid email or password"
            );
        }

        Student currentStudent = student.get();

        if (!currentStudent.getPassword().equals(password)) {
            throw new AuthenticationException(
                    "Invalid email or password"
            );
        }

        if (!currentStudent.isActive()) {
            throw new AuthenticationException(
                    "User is not active"
            );
        }

        AuthenticatedUser user =
                new AuthenticatedUser(
                        currentStudent.getId(),
                        currentStudent.getRole()
                );

        String token = UUID.randomUUID().toString();

        tokenRepository.save(token, user);

        return token;
    }

    public AuthenticatedUser authenticate(String token) {

        if (token == null || token.isEmpty()) {
            throw new AuthenticationException(
                    "Invalid token"
            );
        }

        return tokenRepository.findUserByToken(token)
                .orElseThrow(() ->
                        new AuthenticationException(
                                "Invalid token"
                        ));
    }


}