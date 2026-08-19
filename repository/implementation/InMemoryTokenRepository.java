package com.coursemanagement.repository.implementation;

import com.coursemanagement.auth.AuthenticatedUser;
import com.coursemanagement.repository.interfaces.TokenRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryTokenRepository implements TokenRepository {

    private Map<String, AuthenticatedUser> tokens;

    public InMemoryTokenRepository() {
        tokens = new HashMap<>();
    }

    @Override
    public void save(String token, AuthenticatedUser user) {
        tokens.put(token, user);
    }

    @Override
    public Optional<AuthenticatedUser> findUserByToken(String token) {
        return Optional.ofNullable(tokens.get(token));
    }
}