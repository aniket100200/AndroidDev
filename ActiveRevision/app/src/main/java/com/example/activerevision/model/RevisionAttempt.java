package com.example.activerevision.model;

import java.util.Objects;

public class RevisionAttempt {
    public static final String RATING_EASY = "EASY";
    public static final String RATING_MEDIUM = "MEDIUM";
    public static final String RATING_HARD = "HARD";
    public static final String RATING_FAILED = "FAILED";

    private long id;
    private long revisionTaskId;
    private Integer score; // 0 to 100, nullable
    private String recallRating; // EASY, MEDIUM, HARD, FAILED
    private boolean recalledWithoutNotes;
    private int durationSeconds;
    private String mistakes;
    private String notesAfterReview;
    private long attemptedAt;

    public RevisionAttempt() {
    }

    public RevisionAttempt(long id, long revisionTaskId, Integer score, String recallRating,
                           boolean recalledWithoutNotes, int durationSeconds, String mistakes,
                           String notesAfterReview, long attemptedAt) {
        this.id = id;
        this.revisionTaskId = revisionTaskId;
        this.score = score;
        this.recallRating = recallRating;
        this.recalledWithoutNotes = recalledWithoutNotes;
        this.durationSeconds = durationSeconds;
        this.mistakes = mistakes;
        this.notesAfterReview = notesAfterReview;
        this.attemptedAt = attemptedAt;
    }

    public RevisionAttempt(long revisionTaskId, Integer score, String recallRating,
                           boolean recalledWithoutNotes, int durationSeconds, String mistakes,
                           String notesAfterReview, long attemptedAt) {
        this(-1, revisionTaskId, score, recallRating, recalledWithoutNotes, durationSeconds, mistakes, notesAfterReview, attemptedAt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRevisionTaskId() {
        return revisionTaskId;
    }

    public void setRevisionTaskId(long revisionTaskId) {
        this.revisionTaskId = revisionTaskId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRecallRating() {
        return recallRating;
    }

    public void setRecallRating(String recallRating) {
        this.recallRating = recallRating;
    }

    public boolean isRecalledWithoutNotes() {
        return recalledWithoutNotes;
    }

    public void setRecalledWithoutNotes(boolean recalledWithoutNotes) {
        this.recalledWithoutNotes = recalledWithoutNotes;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getMistakes() {
        return mistakes;
    }

    public void setMistakes(String mistakes) {
        this.mistakes = mistakes;
    }

    public String getNotesAfterReview() {
        return notesAfterReview;
    }

    public void setNotesAfterReview(String notesAfterReview) {
        this.notesAfterReview = notesAfterReview;
    }

    public long getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(long attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RevisionAttempt attempt = (RevisionAttempt) o;
        return id == attempt.id && revisionTaskId == attempt.revisionTaskId &&
                recalledWithoutNotes == attempt.recalledWithoutNotes &&
                durationSeconds == attempt.durationSeconds && attemptedAt == attempt.attemptedAt &&
                Objects.equals(score, attempt.score) && Objects.equals(recallRating, attempt.recallRating) &&
                Objects.equals(mistakes, attempt.mistakes) && Objects.equals(notesAfterReview, attempt.notesAfterReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, revisionTaskId, score, recallRating, recalledWithoutNotes,
                durationSeconds, mistakes, notesAfterReview, attemptedAt);
    }
}
