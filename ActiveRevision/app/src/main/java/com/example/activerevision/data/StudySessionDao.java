package com.example.activerevision.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.activerevision.model.StudySession;

import java.util.ArrayList;
import java.util.List;

public class StudySessionDao {
    private final DatabaseHelper dbHelper;

    public StudySessionDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(StudySession session, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("topic_id", session.getTopicId());
        cv.put("studied_at", session.getStudiedAt());
        cv.put("duration_minutes", session.getDurationMinutes());
        cv.put("notes", session.getNotes());
        cv.put("created_at", session.getCreatedAt());
        return db.insert("study_sessions", null, cv);
    }

    public StudySession getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("study_sessions", null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null)) {
            if (c.moveToFirst()) {
                return parseSession(c);
            }
        }
        return null;
    }

    public List<StudySession> getSessionsForTopic(long topicId) {
        List<StudySession> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("study_sessions", null, "topic_id = ?", new String[]{String.valueOf(topicId)},
                null, null, "studied_at DESC")) {
            while (c.moveToNext()) {
                list.add(parseSession(c));
            }
        }
        return list;
    }

    public int getStudySessionCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM study_sessions", null)) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    private StudySession parseSession(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        long topicId = c.getLong(c.getColumnIndexOrThrow("topic_id"));
        long studiedAt = c.getLong(c.getColumnIndexOrThrow("studied_at"));
        int durationMinutes = c.getInt(c.getColumnIndexOrThrow("duration_minutes"));
        String notes = c.getString(c.getColumnIndexOrThrow("notes"));
        long createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        return new StudySession(id, topicId, studiedAt, durationMinutes, notes, createdAt);
    }
}
