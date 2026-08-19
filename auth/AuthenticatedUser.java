package com.coursemanagement.auth;

import com.coursemanagement.model.Role;

public class AuthenticatedUser {

    private String userId;
    private Role role;

    public AuthenticatedUser(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }
}