package com.example.activerevision.model;

import java.util.Objects;

public class StudySession {
    private long id;
    private long topicId;
    private long studiedAt;
    private int durationMinutes;
    private String notes;
    private long createdAt;

    public StudySession() {
    }

    public StudySession(long id, long topicId, long studiedAt, int durationMinutes, String notes, long createdAt) {
        this.id = id;
        this.topicId = topicId;
        this.studiedAt = studiedAt;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public StudySession(long topicId, long studiedAt, int durationMinutes, String notes, long createdAt) {
        this(-1, topicId, studiedAt, durationMinutes, notes, createdAt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getStudiedAt() {
        return studiedAt;
    }

    public void setStudiedAt(long studiedAt) {
        this.studiedAt = studiedAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
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
        StudySession session = (StudySession) o;
        return id == session.id && topicId == session.topicId && studiedAt == session.studiedAt &&
                durationMinutes == session.durationMinutes && createdAt == session.createdAt &&
                Objects.equals(notes, session.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicId, studiedAt, durationMinutes, notes, createdAt);
    }
}
