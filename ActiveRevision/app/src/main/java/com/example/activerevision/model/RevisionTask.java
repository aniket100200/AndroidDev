package com.example.activerevision.model;

import java.util.Objects;

public class RevisionTask {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_MISSED = "MISSED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private long id;
    private long studySessionId;
    private int intervalDays;
    private long scheduledAt;
    private String status;
    private Long completedAt;
    private long createdAt;

    public RevisionTask() {
    }

    public RevisionTask(long id, long studySessionId, int intervalDays, long scheduledAt,
                        String status, Long completedAt, long createdAt) {
        this.id = id;
        this.studySessionId = studySessionId;
        this.intervalDays = intervalDays;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
    }

    public RevisionTask(long studySessionId, int intervalDays, long scheduledAt, String status, long createdAt) {
        this(-1, studySessionId, intervalDays, scheduledAt, status, null, createdAt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudySessionId() {
        return studySessionId;
    }

    public void setStudySessionId(long studySessionId) {
        this.studySessionId = studySessionId;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public long getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(long scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
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
        RevisionTask task = (RevisionTask) o;
        return id == task.id && studySessionId == task.studySessionId &&
                intervalDays == task.intervalDays && scheduledAt == task.scheduledAt &&
                createdAt == task.createdAt && Objects.equals(status, task.status) &&
                Objects.equals(completedAt, task.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studySessionId, intervalDays, scheduledAt, status, completedAt, createdAt);
    }
}
