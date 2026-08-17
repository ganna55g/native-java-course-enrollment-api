package com.coursemanagement.dto.response;

public class LoginResponse {

    private String studentId;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(String studentId, String message) {
        this.studentId = studentId;
        this.message = message;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}