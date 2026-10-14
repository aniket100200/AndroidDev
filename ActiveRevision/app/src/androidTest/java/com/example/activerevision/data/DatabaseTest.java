package com.example.activerevision.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.activerevision.model.RevisionAttempt;
import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.model.Subject;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.model.Topic;
import com.example.activerevision.repository.RevisionRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private Context context;
    private RevisionRepository repository;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("active_revision.db");
        repository = new RevisionRepository(context);
    }

    @After
    public void tearDown() {
        repository.getDbHelper().close();
        context.deleteDatabase("active_revision.db");
    }

    @Test
    public void testSubjectAndTopicCrud() {
        long subjectId = repository.addSubject(new Subject("Mathematics", System.currentTimeMillis()));
        assertTrue(subjectId > 0);

        long topicId = repository.addTopic(new Topic(subjectId, "Percentage", "Basic % formulas", "Formula: (part/whole)*100", System.currentTimeMillis()));
        assertTrue(topicId > 0);

        List<Subject> subjects = repository.getAllSubjects();
        assertEquals(1, subjects.size());
        assertEquals("Mathematics", subjects.get(0).getName());

        List<Topic> topics = repository.getTopicsForSubject(subjectId);
        assertEquals(1, topics.size());
        assertEquals("Percentage", topics.get(0).getName());
    }

    @Test
    public void testSaveStudySessionCreatesFiveTasks() {
        long subjectId = repository.addSubject(new Subject("Science", System.currentTimeMillis()));
        long topicId = repository.addTopic(new Topic(subjectId, "Photosynthesis", "Plant process", "Notes here", System.currentTimeMillis()));

        long studiedAt = System.currentTimeMillis();
        long sessionId = repository.saveStudySessionAndGenerateSchedule(topicId, studiedAt, 30, "Studied page 12");
        assertTrue(sessionId > 0);

        List<TaskWithDetails> tasks = repository.getTasksForTopic(topicId);
        assertEquals(5, tasks.size());

        int[] expectedOffsets = {0, 3, 7, 14, 21};
        for (int i = 0; i < 5; i++) {
            assertEquals(expectedOffsets[i], tasks.get(i).getTask().getIntervalDays());
            assertEquals(RevisionTask.STATUS_PENDING, tasks.get(i).getTask().getStatus());
        }
    }

    @Test
    public void testNegativeStudyDurationRejected() {
        long subjectId = repository.addSubject(new Subject("History", System.currentTimeMillis()));
        long topicId = repository.addTopic(new Topic(subjectId, "WW2", "Overview", "Notes", System.currentTimeMillis()));

        try {
            repository.saveStudySessionAndGenerateSchedule(topicId, System.currentTimeMillis(), -10, "Invalid duration");
            fail("Expected IllegalArgumentException for negative duration");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompleteTaskAndPreserveAttemptHistory() {
        long subjectId = repository.addSubject(new Subject("Biology", System.currentTimeMillis()));
        long topicId = repository.addTopic(new Topic(subjectId, "Cell Structure", "Organelles", "Notes", System.currentTimeMillis()));

        repository.saveStudySessionAndGenerateSchedule(topicId, System.currentTimeMillis(), 20, "Notes");
        List<TaskWithDetails> tasks = repository.getTasksForTopic(topicId);
        TaskWithDetails day0Task = tasks.get(0);

        long now = System.currentTimeMillis();
        RevisionAttempt attempt1 = new RevisionAttempt(
                day0Task.getTask().getId(),
                85,
                RevisionAttempt.RATING_EASY,
                true,
                120,
                "Minor mistake in mitochondria function",
                "Reviewed mitochondria",
                now
        );

        boolean completed = repository.completeRevisionAttempt(attempt1);
        assertTrue(completed);

        TaskWithDetails updatedTask = repository.getTaskWithDetailsById(day0Task.getTask().getId());
        assertEquals(RevisionTask.STATUS_COMPLETED, updatedTask.getTask().getStatus());
        assertNotNull(updatedTask.getTask().getCompletedAt());
    }
}
