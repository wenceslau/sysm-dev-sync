package com.sysm.devsync.domain.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Note extends AbstractModel {

    private final String id;
    private final Instant createdAt;
    private final String projectId;
    private final String authorId;
    private int version;

    private Instant updatedAt;

    private String title;
    private String content;
    private Set<String> tagsId;

    private Note(String id, Instant createdAt, Instant updatedAt,
                String title, String content, Set<String> tagsId,
                String projectId, String authorId, int version) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.content = content;
        this.tagsId = tagsId;
        this.projectId = projectId;
        this.authorId = authorId;
        this.version = version;
        validate();
    }

    private void validate() {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        if (authorId == null || authorId.isEmpty()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
    }

    public String getId() {
        return id;
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

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Set<String> getTagsId() {
        return Collections.unmodifiableSet(tagsId);
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getVersion() {
        return version;
    }

    public void update(String title, String content) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.title = title;
        this.content = content;
        this.updatedAt = Instant.now();
        this.version++;
    }

    public void updateContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.content = content;
        this.updatedAt = Instant.now();
        this.version++;
    }

    public void addTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }
        if (this.tagsId == null) {
            this.tagsId = new HashSet<>();
        }
        this.tagsId.add(tag);
    }

    public void removeTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }
        if (this.tagsId != null) {
            this.tagsId.remove(tag);
        }
    }

    public static Note create(String title, String content, String projectId, String authorId) {
        String id = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new Note(
                id,
                now,
                now,
                title,
                content,
                new HashSet<>(),
                projectId,
                authorId,
                1
        );
    }

    public static Note build(String id, Instant createdAt, Instant updatedAt,
                             String title, String content, Set<String> tags,
                             String projectId, String authorId, int version) {
        return new Note(
                id,
                createdAt,
                updatedAt,
                title,
                content,
                tags != null ? new HashSet<>(tags) : new HashSet<>(),
                projectId,
                authorId,
                version
        );
    }
}
