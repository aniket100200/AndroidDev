package com.example.activerevision.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.util.DateTimeUtils;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

public class RevisionScheduleGeneratorTest {

    @Test
    public void generateTasksForSession_createsExactlyFiveTasksWithCorrectOffsets() {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.OCTOBER, 12, 9, 30, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long studiedAt = cal.getTimeInMillis();

        long sessionId = 100L;
        List<RevisionTask> tasks = RevisionScheduleGenerator.generateTasksForSession(sessionId, studiedAt);

        assertNotNull(tasks);
        assertEquals(5, tasks.size());

        int[] expectedOffsets = {0, 3, 7, 14, 21};
        for (int i = 0; i < expectedOffsets.length; i++) {
            RevisionTask task = tasks.get(i);
            assertEquals(sessionId, task.getStudySessionId());
            assertEquals(expectedOffsets[i], task.getIntervalDays());
            assertEquals(RevisionTask.STATUS_PENDING, task.getStatus());

            long expectedScheduledAt = DateTimeUtils.calculateScheduledAt(studiedAt, expectedOffsets[i]);
            assertEquals(expectedScheduledAt, task.getScheduledAt());
        }
    }

    @Test
    public void dayZeroTaskIsImmediatelyDueAtStudyTime() {
        long now = System.currentTimeMillis();
        List<RevisionTask> tasks = RevisionScheduleGenerator.generateTasksForSession(1L, now);

        RevisionTask day0Task = tasks.get(0);
        assertEquals(0, day0Task.getIntervalDays());
        assertTrue(day0Task.getScheduledAt() <= System.currentTimeMillis() + 1000);
    }
}
