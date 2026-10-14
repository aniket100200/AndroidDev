package com.example.activerevision.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.activerevision.model.RevisionAttempt;

import java.util.ArrayList;
import java.util.List;

public class RevisionAttemptDao {
    private final DatabaseHelper dbHelper;

    public RevisionAttemptDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(RevisionAttempt attempt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return insert(attempt, db);
    }

    public long insert(RevisionAttempt attempt, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("revision_task_id", attempt.getRevisionTaskId());
        if (attempt.getScore() != null) {
            cv.put("score", attempt.getScore());
        } else {
            cv.putNull("score");
        }
        cv.put("recall_rating", attempt.getRecallRating());
        cv.put("recalled_without_notes", attempt.isRecalledWithoutNotes() ? 1 : 0);
        cv.put("duration_seconds", attempt.getDurationSeconds());
        cv.put("mistakes", attempt.getMistakes());
        cv.put("notes_after_review", attempt.getNotesAfterReview());
        cv.put("attempted_at", attempt.getAttemptedAt());
        return db.insert("revision_attempts", null, cv);
    }

    public List<RevisionAttempt> getAttemptsForTask(long taskId) {
        List<RevisionAttempt> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("revision_attempts", null, "revision_task_id = ?",
                new String[]{String.valueOf(taskId)}, null, null, "attempted_at DESC")) {
            while (c.moveToNext()) {
                list.add(parseAttempt(c));
            }
        }
        return list;
    }

    public RevisionAttempt getLatestAttemptForTask(long taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("revision_attempts", null, "revision_task_id = ?",
                new String[]{String.valueOf(taskId)}, null, null, "attempted_at DESC LIMIT 1")) {
            if (c.moveToFirst()) {
                return parseAttempt(c);
            }
        }
        return null;
    }

    public Double getAverageScore() {
        String sql = "SELECT AVG(score) FROM revision_attempts WHERE score IS NOT NULL";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery(sql, null)) {
            if (c.moveToFirst() && !c.isNull(0)) {
                return c.getDouble(0);
            }
        }
        return null;
    }

    private RevisionAttempt parseAttempt(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        long taskId = c.getLong(c.getColumnIndexOrThrow("revision_task_id"));
        Integer score = c.isNull(c.getColumnIndexOrThrow("score")) ?
                null : c.getInt(c.getColumnIndexOrThrow("score"));
        String rating = c.getString(c.getColumnIndexOrThrow("recall_rating"));
        boolean noNotes = c.getInt(c.getColumnIndexOrThrow("recalled_without_notes")) == 1;
        int duration = c.getInt(c.getColumnIndexOrThrow("duration_seconds"));
        String mistakes = c.getString(c.getColumnIndexOrThrow("mistakes"));
        String notesAfter = c.getString(c.getColumnIndexOrThrow("notes_after_review"));
        long attemptedAt = c.getLong(c.getColumnIndexOrThrow("attempted_at"));

        return new RevisionAttempt(id, taskId, score, rating, noNotes, duration, mistakes, notesAfter, attemptedAt);
    }
}
