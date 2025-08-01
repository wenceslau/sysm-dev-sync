package com.sysm.devsync.domain.models;

public class Tag extends AbstractModel {

    private final String id;
    private String name;
    private String color;
    private String description;
    private String category;
    private int amountUsed;

    private Tag(String id, String name, String color,
                String description, String category, int amountUsed) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.category = category;
        this.amountUsed = amountUsed;
        validate();
    }

    public void validate() {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
    }

    public Tag update(String name, String color) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }

        this.name = name;
        this.color = color;
        return this;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public void incrementUse() {
        this.amountUsed++;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getAmountUsed() {
        return amountUsed;
    }

    public static Tag create(String name, String color) {
        String id = java.util.UUID.randomUUID().toString();
        return new Tag(
                id,
                name,
                color,
                null,
                null,
                0
        );
    }

    public static Tag create(String name, String color, String category) {
        String id = java.util.UUID.randomUUID().toString();
        return new Tag(
                id,
                name,
                color,
                null,
                category,
                0
        );
    }

    public static Tag build(String id, String name, String color,
                            String description, String category, int amountUsed) {
        return new Tag(
                id,
                name,
                color,
                description,
                category,
                amountUsed
        );
    }
}
