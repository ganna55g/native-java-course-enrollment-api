package com.coursemanagement.repository.interfaces;

import com.coursemanagement.auth.AuthenticatedUser;

import java.util.Optional;

public interface TokenRepository {

    void save(String token, AuthenticatedUser user);

    Optional<AuthenticatedUser> findUserByToken(String token);

}