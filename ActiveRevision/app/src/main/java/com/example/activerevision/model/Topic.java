package com.example.activerevision.model;

import java.util.Objects;

public class Topic {
    private long id;
    private long subjectId;
    private String name;
    private String description;
    private String notes;
    private long createdAt;

    public Topic() {
    }

    public Topic(long id, long subjectId, String name, String description, String notes, long createdAt) {
        this.id = id;
        this.subjectId = subjectId;
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Topic(long subjectId, String name, String description, String notes, long createdAt) {
        this(-1, subjectId, name, description, notes, createdAt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id == topic.id && subjectId == topic.subjectId && createdAt == topic.createdAt &&
                Objects.equals(name, topic.name) && Objects.equals(description, topic.description) &&
                Objects.equals(notes, topic.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, name, description, notes, createdAt);
    }
}
