package com.coursemanagement.dto.request;

import com.coursemanagement.model.DiscountType;

public class CreateEnrollmentRequest {

    private String studentId;
    private String courseId;
    private DiscountType discountType;

    public CreateEnrollmentRequest() {
    }

    public CreateEnrollmentRequest(
            String studentId,
            String courseId,
            DiscountType discountType) {

        this.studentId = studentId;
        this.courseId = courseId;
        this.discountType = discountType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}