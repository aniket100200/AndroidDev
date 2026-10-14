package com.example.activerevision.scheduler;

import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class RevisionScheduleGenerator {

    public static final int[] DEFAULT_INTERVAL_DAYS = {0, 3, 7, 14, 21};

    public static List<RevisionTask> generateTasksForSession(long sessionId, long studiedAt) {
        long now = System.currentTimeMillis();
        List<RevisionTask> tasks = new ArrayList<>();

        for (int intervalDays : DEFAULT_INTERVAL_DAYS) {
            long scheduledAt = DateTimeUtils.calculateScheduledAt(studiedAt, intervalDays);
            RevisionTask task = new RevisionTask(
                    sessionId,
                    intervalDays,
                    scheduledAt,
                    RevisionTask.STATUS_PENDING,
                    now
            );
            tasks.add(task);
        }

        return tasks;
    }
}
