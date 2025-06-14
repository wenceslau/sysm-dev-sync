package com.sysm.devsync.domain.models;

import com.sysm.devsync.domain.enums.StatusQuestion;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Question extends AbstractModel {

    /*
        id: UUID
        title
        description: Markdown or HTML
        tags: [String]
        projectId: Project reference
        authorId: User reference
        status: OPEN / CLOSED / RESOLVED
        createdAt, updatedAt
    */

    private final String id;
    private final Instant createdAt;
    private final String projectId;
    private final String authorId;
    private final Set<String> tagsId;

    private String title;
    private String description;

    private StatusQuestion statusQuestion; // OPEN, CLOSED, RESOLVED
    private Instant updatedAt;


    private Question(String id, Instant createdAt, String projectId,
                     String authorId, Set<String> tagsId, String title,
                     String description, StatusQuestion statusQuestion, Instant updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.projectId = projectId;
        this.authorId = authorId;
        this.tagsId = tagsId;
        this.title = title;
        this.description = description;
        this.statusQuestion = statusQuestion;
        this.updatedAt = updatedAt;
        validate();
    }

    private void validate() {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        if (authorId == null || authorId.isEmpty()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
    }

    public Question update(String title, String description) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }

        this.updatedAt = Instant.now();
        this.title = title;
        this.description = description;
        return this;
    }

    public void changeStatus(StatusQuestion statusQuestion) {
        if (statusQuestion == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        this.updatedAt = Instant.now();
        this.statusQuestion = statusQuestion;
    }

    public void addTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }
        this.tagsId.add(tag);
    }

    public void removeTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }
        this.tagsId.remove(tag);
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTagsId() {
        return tagsId;
    }

    public StatusQuestion getStatus() {
        return statusQuestion;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static Question create(String title, String description, String projectId, String authorId) {
        String id = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new Question(
                id,
                now,
                projectId,
                authorId,
                new HashSet<>(),
                title,
                description,
                StatusQuestion.OPEN,
                now
        );
    }

    public static Question build(String id, Instant createdAt, Instant updatedAt,
                                 String title, String description, Set<String> tags,
                                 String projectId, String authorId, StatusQuestion statusQuestion) {
        return new Question(
                id,
                createdAt,
                projectId,
                authorId,
                new HashSet<>(tags),
                title,
                description,
                statusQuestion,
                updatedAt
        );
    }
}
