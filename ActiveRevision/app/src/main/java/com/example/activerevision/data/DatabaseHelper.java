package com.example.activerevision.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "active_revision.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE subjects (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL COLLATE NOCASE UNIQUE, " +
                "created_at INTEGER NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE topics (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL COLLATE NOCASE, " +
                "description TEXT, " +
                "notes TEXT, " +
                "created_at INTEGER NOT NULL, " +
                "FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE, " +
                "UNIQUE(subject_id, name)" +
                ");");

        db.execSQL("CREATE TABLE study_sessions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "topic_id INTEGER NOT NULL, " +
                "studied_at INTEGER NOT NULL, " +
                "duration_minutes INTEGER NOT NULL DEFAULT 0 CHECK(duration_minutes >= 0), " +
                "notes TEXT, " +
                "created_at INTEGER NOT NULL, " +
                "FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE" +
                ");");

        db.execSQL("CREATE TABLE revision_tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "study_session_id INTEGER NOT NULL, " +
                "interval_days INTEGER NOT NULL CHECK(interval_days IN (0, 3, 7, 14, 21)), " +
                "scheduled_at INTEGER NOT NULL, " +
                "status TEXT NOT NULL DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'MISSED', 'CANCELLED')), " +
                "completed_at INTEGER, " +
                "created_at INTEGER NOT NULL, " +
                "FOREIGN KEY (study_session_id) REFERENCES study_sessions(id) ON DELETE CASCADE, " +
                "UNIQUE(study_session_id, interval_days)" +
                ");");

        db.execSQL("CREATE TABLE revision_attempts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "revision_task_id INTEGER NOT NULL, " +
                "score INTEGER CHECK(score IS NULL OR (score >= 0 AND score <= 100)), " +
                "recall_rating TEXT CHECK(recall_rating IS NULL OR recall_rating IN ('EASY', 'MEDIUM', 'HARD', 'FAILED')), " +
                "recalled_without_notes INTEGER NOT NULL DEFAULT 0 CHECK(recalled_without_notes IN (0, 1)), " +
                "duration_seconds INTEGER NOT NULL DEFAULT 0 CHECK(duration_seconds >= 0), " +
                "mistakes TEXT, " +
                "notes_after_review TEXT, " +
                "attempted_at INTEGER NOT NULL, " +
                "FOREIGN KEY (revision_task_id) REFERENCES revision_tasks(id) ON DELETE CASCADE" +
                ");");

        db.execSQL("CREATE INDEX idx_revision_status_due ON revision_tasks(status, scheduled_at);");
        db.execSQL("CREATE INDEX idx_revision_session_interval ON revision_tasks(study_session_id, interval_days);");
        db.execSQL("CREATE INDEX idx_study_topic_date ON study_sessions(topic_id, studied_at DESC);");
        db.execSQL("CREATE INDEX idx_attempt_task_date ON revision_attempts(revision_task_id, attempted_at DESC);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic preserved per spec without dropping user data unless necessary
    }
}
