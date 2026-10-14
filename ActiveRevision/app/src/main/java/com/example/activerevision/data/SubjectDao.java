package com.example.activerevision.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.activerevision.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectDao {
    private final DatabaseHelper dbHelper;

    public SubjectDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(Subject subject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", subject.getName());
        cv.put("created_at", subject.getCreatedAt());
        return db.insert("subjects", null, cv);
    }

    public boolean update(Subject subject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", subject.getName());
        int rows = db.update("subjects", cv, "id = ?", new String[]{String.valueOf(subject.getId())});
        return rows > 0;
    }

    public boolean delete(long subjectId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("subjects", "id = ?", new String[]{String.valueOf(subjectId)});
        return rows > 0;
    }

    public Subject getById(long subjectId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("subjects", null, "id = ?", new String[]{String.valueOf(subjectId)},
                null, null, null)) {
            if (c.moveToFirst()) {
                return parseSubject(c);
            }
        }
        return null;
    }

    public List<Subject> getAll() {
        List<Subject> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query("subjects", null, null, null,
                null, null, "name ASC")) {
            while (c.moveToNext()) {
                list.add(parseSubject(c));
            }
        }
        return list;
    }

    public int getSubjectCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM subjects", null)) {
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        }
        return 0;
    }

    private Subject parseSubject(Cursor c) {
        long id = c.getLong(c.getColumnIndexOrThrow("id"));
        String name = c.getString(c.getColumnIndexOrThrow("name"));
        long createdAt = c.getLong(c.getColumnIndexOrThrow("created_at"));
        return new Subject(id, name, createdAt);
    }
}
