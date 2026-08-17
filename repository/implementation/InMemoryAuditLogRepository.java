package com.coursemanagement.repository.implementation;

import com.coursemanagement.model.AuditLog;
import com.coursemanagement.repository.interfaces.AuditLogRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class InMemoryAuditLogRepository implements AuditLogRepository {

    private Map<String, AuditLog> auditLogs = new HashMap<>();

    private int nextId = 1;

    @Override
    public AuditLog save(AuditLog auditLog) {

        if (auditLog.getId() == null || auditLog.getId().isEmpty()) {
            auditLog.setId("A" + nextId);
            nextId++;
        }

        auditLogs.put(auditLog.getId(), auditLog);

        return auditLog;
    }

    @Override
    public Optional<AuditLog> findById(String id) {

        AuditLog auditLog = auditLogs.get(id);

        return Optional.ofNullable(auditLog);
    }

    @Override
    public Map<String, AuditLog> findAll() {
        return auditLogs;
    }

    @Override
    public List<AuditLog> findByEntityType(String entityType) {

        List<AuditLog> result = new ArrayList<>();

        for (AuditLog auditLog : auditLogs.values()) {

            if (auditLog.getEntityType().equals(entityType)) {
                result.add(auditLog);
            }
        }

        return result;
    }
}