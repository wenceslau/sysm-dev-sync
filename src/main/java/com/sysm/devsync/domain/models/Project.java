package com.sysm.devsync.domain.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Project extends AbstractModel {

    private final String id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String description;
    private String workspaceId;

    private Project(String id, Instant createdAt, Instant updatedAt, String name, String description, String workspaceId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.workspaceId = workspaceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate(name, description);
    }

    private void validate(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Project description cannot be null or blank");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public Instant getCreatedAt() {
        if (createdAt != null) {
            return createdAt.truncatedTo(ChronoUnit.MILLIS);
        }
        return null;
    }

    public Instant getUpdatedAt() {
        if (updatedAt != null) {
            return updatedAt.truncatedTo(ChronoUnit.MILLIS);
        }
        return null;
    }
    public Project update(String name, String description) {
        validate(name, description);
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    public void changeWorkspace(String newWorkspaceId) {
        if (newWorkspaceId == null || newWorkspaceId.isBlank()) {
            throw new IllegalArgumentException("New workspace ID cannot be null or blank");
        }
        this.workspaceId = newWorkspaceId;
        this.updatedAt = Instant.now();
    }

    public static Project create(String name, String description, String workspaceId) {
        String id = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new Project(id, now, now, name, description, workspaceId);
    }

    public static Project build(String id, String name, String description, String workspaceId, Instant createdAt, Instant updatedAt) {
        return new Project(id, createdAt, updatedAt, name, description, workspaceId);
    }

}
