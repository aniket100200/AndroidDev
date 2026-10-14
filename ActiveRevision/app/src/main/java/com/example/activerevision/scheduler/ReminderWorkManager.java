package com.example.activerevision.scheduler;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ReminderWorkManager {

    private static final String PERIODIC_WORK_TAG = "revision_reminder_periodic_work";

    public static void schedulePeriodicReminder(Context context) {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                RevisionReminderWorker.class,
                15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }

    public static void triggerImmediateCheck(Context context) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(
                RevisionReminderWorker.class
        ).build();

        WorkManager.getInstance(context).enqueue(workRequest);
    }

    public static void cancelPeriodicReminder(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK_TAG);
    }
}
