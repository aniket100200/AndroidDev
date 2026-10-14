package com.example.activerevision.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.activerevision.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicDao {
    private final DatabaseHelper dbHelper;

    public TopicDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(Topic topic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject_id", topic.getSubjectId());
        cv.put("name", topic.getName());
        cv.put("description", topic.getDescription());
        cv.put("notes", topic.getNotes());
        cv.put("created_at", topic.getCreatedAt());
        return db.insert("topics", null, cv);
    }

    public boolean update(Topic topic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject_id", topic.getSubjectId());
        cv.put("name", topic.getName());
        cv.put("description", topic.getDescription());
        cv.put("notes", topic.getNotes());
        int rows = db.update("topics", cv, "id = ?", new String[]{String.valueOf(topic.getId())});
        return rows > 0;
    }

    public boolean delete(long topicId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("topics", "id = ?", new String[]{String.valueOf(topicId)});
        return rows > 0;
    }

    public Topic getById(long topicId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("topics", null, "id = ?", new String[]{String.valueOf(topicId)},
                null, null, null)) {
            if (c.moveToFirst()) {
                return parseTopic(c);
            }
        }
        return null;
    }

    public List<Topic> getTopicsForSubject(long subjectId) {
        List<Topic> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("topics", null, "subject_id = ?", new String[]{String.valueOf(subjectId)},
                null, null, "name ASC")) {
            while (c.moveToNext()) {
                list.add(parseTopic(c));
            }
        }
        return list;
    }

    public List<Topic> getAll() {
        List<Topic> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("topics", null, null, null,
                null, null, "name ASC")) {
            while (c.moveToNext()) {
                list.add(parseTopic(c));
            }
        }
        return list;
    }

    public int getTopicCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM topics", null)) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    private Topic parseTopic(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        long subjectId = c.getLong(c.getColumnIndexOrThrow("subject_id"));
        String name = c.getString(c.getColumnIndexOrThrow("name"));
        String description = c.getString(c.getColumnIndexOrThrow("description"));
        String notes = c.getString(c.getColumnIndexOrThrow("notes"));
        long createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        return new Topic(id, subjectId, name, description, notes, createdAt);
    }
}
