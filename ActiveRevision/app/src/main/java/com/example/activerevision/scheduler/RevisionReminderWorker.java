package com.example.activerevision.scheduler;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.notification.NotificationHelper;
import com.example.activerevision.repository.RevisionRepository;

import java.util.List;

public class RevisionReminderWorker extends Worker {

    public RevisionReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        RevisionRepository repository = RevisionRepository.getInstance(context);

        SharedPreferences prefs = context.getSharedPreferences("active_revision_prefs", Context.MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        long gracePeriodHours = prefs.getLong("grace_period_hours", 24L);

        // Reconcile missed tasks
        repository.reconcileMissedTasks(gracePeriodHours);

        if (notificationsEnabled) {
            List<TaskWithDetails> dueTasks = repository.getDueOrOverdueTasks();
            for (TaskWithDetails taskDetails : dueTasks) {
                NotificationHelper.postRevisionReminderNotification(
                        context,
                        taskDetails.getTask().getId(),
                        taskDetails.getTopicName(),
                        taskDetails.getSubjectName(),
                        taskDetails.getTask().getIntervalDays()
                );
            }
        }

        return Result.success();
    }
}
