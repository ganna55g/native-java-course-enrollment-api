package com.coursemanagement.repository.interfaces;

import com.coursemanagement.model.AuditLog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findById(String id);

    Map<String, AuditLog> findAll();

    List<AuditLog> findByEntityType(String entityType);

}