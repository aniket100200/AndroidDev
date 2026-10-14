package com.example.activerevision.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.model.TaskWithDetails;

import java.util.ArrayList;
import java.util.List;

public class RevisionTaskDao {
    private final DatabaseHelper dbHelper;

    public RevisionTaskDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(RevisionTask task, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("study_session_id", task.getStudySessionId());
        cv.put("interval_days", task.getIntervalDays());
        cv.put("scheduled_at", task.getScheduledAt());
        cv.put("status", task.getStatus());
        if (task.getCompletedAt() != null) {
            cv.put("completed_at", task.getCompletedAt());
        }
        cv.put("created_at", task.getCreatedAt());
        return db.insert("revision_tasks", null, cv);
    }

    public boolean updateStatus(long taskId, String status, Long completedAt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return updateStatus(taskId, status, completedAt, db);
    }

    public boolean updateStatus(long taskId, String status, Long completedAt, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        if (completedAt != null) {
            cv.put("completed_at", completedAt);
        } else {
            cv.putNull("completed_at");
        }
        int rows = db.update("revision_tasks", cv, "id = ?", new String[]{String.valueOf(taskId)});
        return rows > 0;
    }

    public RevisionTask getById(long taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("revision_tasks", null, "id = ?", new String[]{String.valueOf(taskId)},
                null, null, null)) {
            if (c.moveToFirst()) {
                return parseTask(c);
            }
        }
        return null;
    }

    public TaskWithDetails getTaskWithDetailsById(long taskId) {
        String sql = "SELECT rt.id AS task_id, rt.study_session_id, rt.interval_days, rt.scheduled_at, rt.status, " +
                "rt.completed_at, rt.created_at AS task_created_at, " +
                "ss.studied_at, ss.notes AS session_notes, " +
                "t.id AS topic_id, t.name AS topic_name, t.description AS topic_description, t.notes AS topic_notes, " +
                "s.id AS subject_id, s.name AS subject_name " +
                "FROM revision_tasks rt " +
                "JOIN study_sessions ss ON ss.id = rt.study_session_id " +
                "JOIN topics t ON t.id = ss.topic_id " +
                "JOIN subjects s ON s.id = t.subject_id " +
                "WHERE rt.id = ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(taskId)})) {
            if (c.moveToFirst()) {
                return parseTaskWithDetails(c);
            }
        }
        return null;
    }

    public List<TaskWithDetails> getDueOrOverdueTasks(long nowMillis) {
        String sql = "SELECT rt.id AS task_id, rt.study_session_id, rt.interval_days, rt.scheduled_at, rt.status, " +
                "rt.completed_at, rt.created_at AS task_created_at, " +
                "ss.studied_at, ss.notes AS session_notes, " +
                "t.id AS topic_id, t.name AS topic_name, t.description AS topic_description, t.notes AS topic_notes, " +
                "s.id AS subject_id, s.name AS subject_name " +
                "FROM revision_tasks rt " +
                "JOIN study_sessions ss ON ss.id = rt.study_session_id " +
                "JOIN topics t ON t.id = ss.topic_id " +
                "JOIN subjects s ON s.id = t.subject_id " +
                "WHERE rt.status IN ('PENDING', 'IN_PROGRESS', 'MISSED') AND rt.scheduled_at <= ? " +
                "ORDER BY rt.scheduled_at ASC, rt.id ASC";
        return queryTaskDetailsList(sql, new String[]{String.valueOf(nowMillis)});
    }

    public List<TaskWithDetails> getUpcomingTasks(long nowMillis) {
        String sql = "SELECT rt.id AS task_id, rt.study_session_id, rt.interval_days, rt.scheduled_at, rt.status, " +
                "rt.completed_at, rt.created_at AS task_created_at, " +
                "ss.studied_at, ss.notes AS session_notes, " +
                "t.id AS topic_id, t.name AS topic_name, t.description AS topic_description, t.notes AS topic_notes, " +
                "s.id AS subject_id, s.name AS subject_name " +
                "FROM revision_tasks rt " +
                "JOIN study_sessions ss ON ss.id = rt.study_session_id " +
                "JOIN topics t ON t.id = ss.topic_id " +
                "JOIN subjects s ON s.id = t.subject_id " +
                "WHERE rt.status IN ('PENDING', 'IN_PROGRESS') AND rt.scheduled_at > ? " +
                "ORDER BY rt.scheduled_at ASC, rt.id ASC";
        return queryTaskDetailsList(sql, new String[]{String.valueOf(nowMillis)});
    }

    public List<TaskWithDetails> getCompletedTasks() {
        String sql = "SELECT rt.id AS task_id, rt.study_session_id, rt.interval_days, rt.scheduled_at, rt.status, " +
                "rt.completed_at, rt.created_at AS task_created_at, " +
                "ss.studied_at, ss.notes AS session_notes, " +
                "t.id AS topic_id, t.name AS topic_name, t.description AS topic_description, t.notes AS topic_notes, " +
                "s.id AS subject_id, s.name AS subject_name " +
                "FROM revision_tasks rt " +
                "JOIN study_sessions ss ON ss.id = rt.study_session_id " +
                "JOIN topics t ON t.id = ss.topic_id " +
                "JOIN subjects s ON s.id = t.subject_id " +
                "WHERE rt.status = 'COMPLETED' " +
                "ORDER BY rt.completed_at DESC, rt.id DESC";
        return queryTaskDetailsList(sql, null);
    }

    public List<TaskWithDetails> getTasksForTopic(long topicId) {
        String sql = "SELECT rt.id AS task_id, rt.study_session_id, rt.interval_days, rt.scheduled_at, rt.status, " +
                "rt.completed_at, rt.created_at AS task_created_at, " +
                "ss.studied_at, ss.notes AS session_notes, " +
                "t.id AS topic_id, t.name AS topic_name, t.description AS topic_description, t.notes AS topic_notes, " +
                "s.id AS subject_id, s.name AS subject_name " +
                "FROM revision_tasks rt " +
                "JOIN study_sessions ss ON ss.id = rt.study_session_id " +
                "JOIN topics t ON t.id = ss.topic_id " +
                "JOIN subjects s ON s.id = t.subject_id " +
                "WHERE t.id = ? " +
                "ORDER BY rt.scheduled_at ASC";
        return queryTaskDetailsList(sql, new String[]{String.valueOf(topicId)});
    }

    public int getDueTodayCount(long startOfDay, long endOfDay) {
        String sql = "SELECT COUNT(*) FROM revision_tasks " +
                "WHERE status IN ('PENDING', 'IN_PROGRESS', 'MISSED') AND scheduled_at >= ? AND scheduled_at <= ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay)})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    public int getOverdueCount(long startOfDay) {
        String sql = "SELECT COUNT(*) FROM revision_tasks " +
                "WHERE status IN ('PENDING', 'IN_PROGRESS', 'MISSED') AND scheduled_at < ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(startOfDay)})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    public int getUpcomingCount(long endOfDay) {
        String sql = "SELECT COUNT(*) FROM revision_tasks " +
                "WHERE status IN ('PENDING', 'IN_PROGRESS') AND scheduled_at > ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(endOfDay)})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    public int getCompletedCountThisWeek(long startOfWeekMillis) {
        String sql = "SELECT COUNT(*) FROM revision_tasks " +
                "WHERE status = 'COMPLETED' AND completed_at >= ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(startOfWeekMillis)})) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    public double getCompletionRate(long nowMillis) {
        String sqlTotal = "SELECT COUNT(*) FROM revision_tasks WHERE scheduled_at <= ? AND status != 'CANCELLED'";
        String sqlCompleted = "SELECT COUNT(*) FROM revision_tasks WHERE status = 'COMPLETED' AND scheduled_at <= ?";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int total = 0;
        int completed = 0;

        try (Cursor c = db.rawQuery(sqlTotal, new String[]{String.valueOf(nowMillis)})) {
            if (c.moveToFirst()) {
                total = c.getInt(0);
            }
        }
        try (Cursor c = db.rawQuery(sqlCompleted, new String[]{String.valueOf(nowMillis)})) {
            if (c.moveToFirst()) {
                completed = c.getInt(0);
            }
        }

        if (total == 0) return 0.0;
        return ((double) completed / total) * 100.0;
    }

    public int markTasksMissedPastGracePeriod(long thresholdMillis) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", RevisionTask.STATUS_MISSED);
        return db.update("revision_tasks", cv,
                "status IN ('PENDING', 'IN_PROGRESS') AND scheduled_at < ?",
                new String[]{String.valueOf(thresholdMillis)});
    }

    private List<TaskWithDetails> queryTaskDetailsList(String sql, String[] args) {
        List<TaskWithDetails> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, args)) {
            while (c.moveToNext()) {
                list.add(parseTaskWithDetails(c));
            }
        }
        return list;
    }

    private RevisionTask parseTask(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        long studySessionId = c.getLong(c.getColumnIndexOrThrow("study_session_id"));
        int intervalDays = c.getInt(c.getColumnIndexOrThrow("interval_days"));
        long scheduledAt = c.getLong(c.getColumnIndexOrThrow("scheduled_at"));
        String status = c.getString(c.getColumnIndexOrThrow("status"));
        Long completedAt = c.isNull(c.getColumnIndexOrThrow("completed_at")) ?
                null : c.getLong(c.getColumnIndexOrThrow("completed_at"));
        long createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        return new RevisionTask(id, studySessionId, intervalDays, scheduledAt, status, completedAt, createdAt);
    }

    private TaskWithDetails parseTaskWithDetails(Cursor c) {
        long taskId = c.getLong(c.getColumnIndexOrThrow("task_id"));
        long studySessionId = c.getLong(c.getColumnIndexOrThrow("study_session_id"));
        int intervalDays = c.getInt(c.getColumnIndexOrThrow("interval_days"));
        long scheduledAt = c.getLong(c.getColumnIndexOrThrow("scheduled_at"));
        String status = c.getString(c.getColumnIndexOrThrow("status"));
        Long completedAt = c.isNull(c.getColumnIndexOrThrow("completed_at")) ?
                null : c.getLong(c.getColumnIndexOrThrow("completed_at"));
        long taskCreatedAt = c.getLong(c.getColumnIndexOrThrow("task_created_at"));

        RevisionTask task = new RevisionTask(taskId, studySessionId, intervalDays, scheduledAt, status, completedAt, taskCreatedAt);

        long topicId = c.getLong(c.getColumnIndexOrThrow("topic_id"));
        String topicName = c.getString(c.getColumnIndexOrThrow("topic_name"));
        long subjectId = c.getLong(c.getColumnIndexOrThrow("subject_id"));
        String subjectName = c.getString(c.getColumnIndexOrThrow("subject_name"));
        String topicDesc = c.getString(c.getColumnIndexOrThrow("topic_description"));
        String topicNotes = c.getString(c.getColumnIndexOrThrow("topic_notes"));
        String sessionNotes = c.getString(c.getColumnIndexOrThrow("session_notes"));
        long studiedAt = c.getLong(c.getColumnIndexOrThrow("studied_at"));

        return new TaskWithDetails(task, topicId, topicName, subjectId, subjectName, topicDesc, topicNotes, sessionNotes, studiedAt);
    }
}
