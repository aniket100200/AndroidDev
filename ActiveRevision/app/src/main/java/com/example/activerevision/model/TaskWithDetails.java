package com.example.activerevision.model;

public class TaskWithDetails {
    private RevisionTask task;
    private long topicId;
    private String topicName;
    private long subjectId;
    private String subjectName;
    private String topicDescription;
    private String topicNotes;
    private String studySessionNotes;
    private long studiedAt;

    public TaskWithDetails() {
    }

    public TaskWithDetails(RevisionTask task, long topicId, String topicName, long subjectId,
                           String subjectName, String topicDescription, String topicNotes,
                           String studySessionNotes, long studiedAt) {
        this.task = task;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.topicDescription = topicDescription;
        this.topicNotes = topicNotes;
        this.studySessionNotes = studySessionNotes;
        this.studiedAt = studiedAt;
    }

    public RevisionTask getTask() {
        return task;
    }

    public void setTask(RevisionTask task) {
        this.task = task;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getTopicNotes() {
        return topicNotes;
    }

    public void setTopicNotes(String topicNotes) {
        this.topicNotes = topicNotes;
    }

    public String getStudySessionNotes() {
        return studySessionNotes;
    }

    public void setStudySessionNotes(String studySessionNotes) {
        this.studySessionNotes = studySessionNotes;
    }

    public long getStudiedAt() {
        return studiedAt;
    }

    public void setStudiedAt(long studiedAt) {
        this.studiedAt = studiedAt;
    }
}
