package com.sysm.devsync.domain.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Answer extends AbstractModel {

    private final String id;
    private final String questionId;
    private final String authorId;
    private final Instant createdAt;
    private String content;
    private boolean isAccepted;
    private Instant updatedAt;

    private Answer(String id, String questionId, String authorId,
                  Instant createdAt, String content, boolean isAccepted,
                  Instant updatedAt) {
        this.id = id;
        this.questionId = questionId;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.content = content;
        this.isAccepted = isAccepted;
        this.updatedAt = updatedAt;
        validate();
    }

    public void validate() {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (authorId == null || authorId.isBlank()) {
            throw new IllegalArgumentException("Author ID cannot be null or empty");
        }
        if (questionId == null || questionId.isBlank()) {
            throw new IllegalArgumentException("Question ID cannot be null or empty");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
    }

    public Answer update(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.content = content;
        this.updatedAt = Instant.now();
        return this;
    }

    public void accept() {
        this.isAccepted = true;
        this.updatedAt = Instant.now();
    }

    public void reject() {
        this.isAccepted = false;
        this.updatedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public boolean isAccepted() {
        return isAccepted;
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

    public static Answer create(String content, String questionId, String authorId) {
        String id = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new Answer(
                id,
                questionId,
                authorId,
                now,
                content,
                false,
                now
        );
    }

    public static Answer build(String id, String questionId, String authorId,
                               Instant createdAt, String content, boolean isAccepted,
                               Instant updatedAt) {
        return new Answer(
                id,
                questionId,
                authorId,
                createdAt,
                content,
                isAccepted,
                updatedAt
        );
    }
}
