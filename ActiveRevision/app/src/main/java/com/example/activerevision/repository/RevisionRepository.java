package com.example.activerevision.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.example.activerevision.data.DatabaseHelper;
import com.example.activerevision.data.RevisionAttemptDao;
import com.example.activerevision.data.RevisionTaskDao;
import com.example.activerevision.data.StudySessionDao;
import com.example.activerevision.data.SubjectDao;
import com.example.activerevision.data.TopicDao;
import com.example.activerevision.model.RevisionAttempt;
import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.model.StudySession;
import com.example.activerevision.model.Subject;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.model.Topic;
import com.example.activerevision.scheduler.RevisionScheduleGenerator;
import com.example.activerevision.util.DateTimeUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RevisionRepository {

    private static RevisionRepository instance;

    private final DatabaseHelper dbHelper;
    private final SubjectDao subjectDao;
    private final TopicDao topicDao;
    private final StudySessionDao studySessionDao;
    private final RevisionTaskDao revisionTaskDao;
    private final RevisionAttemptDao revisionAttemptDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback<T> {
        void onResult(T result);
    }

    public static synchronized RevisionRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RevisionRepository(context.getApplicationContext());
        }
        return instance;
    }

    public RevisionRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        subjectDao = new SubjectDao(dbHelper);
        topicDao = new TopicDao(dbHelper);
        studySessionDao = new StudySessionDao(dbHelper);
        revisionTaskDao = new RevisionTaskDao(dbHelper);
        revisionAttemptDao = new RevisionAttemptDao(dbHelper);
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    // --- Subject Operations ---
    public long addSubject(Subject subject) {
        return subjectDao.insert(subject);
    }

    public boolean updateSubject(Subject subject) {
        return subjectDao.update(subject);
    }

    public boolean deleteSubject(long subjectId) {
        return subjectDao.delete(subjectId);
    }

    public List<Subject> getAllSubjects() {
        return subjectDao.getAll();
    }

    public Subject getSubjectById(long id) {
        return subjectDao.getById(id);
    }

    // --- Topic Operations ---
    public long addTopic(Topic topic) {
        return topicDao.insert(topic);
    }

    public boolean updateTopic(Topic topic) {
        return topicDao.update(topic);
    }

    public boolean deleteTopic(long topicId) {
        return topicDao.delete(topicId);
    }

    public List<Topic> getTopicsForSubject(long subjectId) {
        return topicDao.getTopicsForSubject(subjectId);
    }

    public Topic getTopicById(long id) {
        return topicDao.getById(id);
    }

    // --- Study Session & Schedule Creation (Transactional) ---
    public long saveStudySessionAndGenerateSchedule(long topicId, long studiedAt, int durationMinutes, String notes) {
        if (topicId <= 0 || studiedAt <= 0 || durationMinutes < 0) {
            throw new IllegalArgumentException("Invalid study session parameters");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long sessionId = -1;
        db.beginTransaction();
        try {
            long now = System.currentTimeMillis();
            StudySession session = new StudySession(topicId, studiedAt, durationMinutes, notes, now);
            sessionId = studySessionDao.insert(session, db);

            if (sessionId <= 0) {
                throw new IllegalStateException("Failed to insert study session");
            }

            List<RevisionTask> tasks = RevisionScheduleGenerator.generateTasksForSession(sessionId, studiedAt);
            for (RevisionTask task : tasks) {
                long taskId = revisionTaskDao.insert(task, db);
                if (taskId <= 0) {
                    throw new IllegalStateException("Failed to insert revision task for interval " + task.getIntervalDays());
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return sessionId;
    }

    // --- Revision Task & Attempt Operations (Transactional) ---
    public boolean completeRevisionAttempt(RevisionAttempt attempt) {
        if (attempt == null || attempt.getRevisionTaskId() <= 0) {
            return false;
        }
        if (attempt.getScore() != null && (attempt.getScore() < 0 || attempt.getScore() > 100)) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            long attemptId = revisionAttemptDao.insert(attempt, db);
            if (attemptId <= 0) {
                return false;
            }

            boolean updated = revisionTaskDao.updateStatus(
                    attempt.getRevisionTaskId(),
                    RevisionTask.STATUS_COMPLETED,
                    attempt.getAttemptedAt(),
                    db
            );

            if (!updated) {
                return false;
            }

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public TaskWithDetails getTaskWithDetailsById(long taskId) {
        return revisionTaskDao.getTaskWithDetailsById(taskId);
    }

    public List<TaskWithDetails> getDueOrOverdueTasks() {
        return revisionTaskDao.getDueOrOverdueTasks(System.currentTimeMillis());
    }

    public List<TaskWithDetails> getUpcomingTasks() {
        return revisionTaskDao.getUpcomingTasks(System.currentTimeMillis());
    }

    public List<TaskWithDetails> getCompletedTasks() {
        return revisionTaskDao.getCompletedTasks();
    }

    public List<TaskWithDetails> getTasksForTopic(long topicId) {
        return revisionTaskDao.getTasksForTopic(topicId);
    }

    public boolean updateTaskStatus(long taskId, String status) {
        return revisionTaskDao.updateStatus(taskId, status, null);
    }

    public int reconcileMissedTasks(long gracePeriodHours) {
        long graceMillis = gracePeriodHours * 60 * 60 * 1000L;
        long threshold = System.currentTimeMillis() - graceMillis;
        return revisionTaskDao.markTasksMissedPastGracePeriod(threshold);
    }

    // --- Dashboard Metrics ---
    public DashboardMetrics getDashboardMetrics() {
        long now = System.currentTimeMillis();
        long startOfDay = DateTimeUtils.getStartOfDay(now);
        long endOfDay = DateTimeUtils.getEndOfDay(now);

        int totalTopics = topicDao.getTopicCount();
        int totalSessions = studySessionDao.getStudySessionCount();
        int dueTodayCount = revisionTaskDao.getDueTodayCount(startOfDay, endOfDay);
        int overdueCount = revisionTaskDao.getOverdueCount(startOfDay);
        int upcomingCount = revisionTaskDao.getUpcomingCount(endOfDay);
        int completedThisWeek = revisionTaskDao.getCompletedCountThisWeek(now - (7 * 24 * 60 * 60 * 1000L));
        double completionRate = revisionTaskDao.getCompletionRate(now);
        Double avgScore = revisionAttemptDao.getAverageScore();

        return new DashboardMetrics(totalTopics, totalSessions, dueTodayCount, overdueCount,
                upcomingCount, completedThisWeek, completionRate, avgScore);
    }

    // --- Async helper methods for UI thread calls ---
    public <T> void executeAsync(Supplier<T> supplier, Callback<T> callback) {
        executor.execute(() -> {
            T result = supplier.get();
            if (callback != null) {
                mainHandler.post(() -> callback.onResult(result));
            }
        });
    }

    public interface Supplier<T> {
        T get();
    }

    public static class DashboardMetrics {
        public final int totalTopics;
        public final int totalSessions;
        public final int dueTodayCount;
        public final int overdueCount;
        public final int upcomingCount;
        public final int completedThisWeek;
        public final double completionRate;
        public final Double averageScore;

        public DashboardMetrics(int totalTopics, int totalSessions, int dueTodayCount,
                                int overdueCount, int upcomingCount, int completedThisWeek,
                                double completionRate, Double averageScore) {
            this.totalTopics = totalTopics;
            this.totalSessions = totalSessions;
            this.dueTodayCount = dueTodayCount;
            this.overdueCount = overdueCount;
            this.upcomingCount = upcomingCount;
            this.completedThisWeek = completedThisWeek;
            this.completionRate = completionRate;
            this.averageScore = averageScore;
        }
    }
}
