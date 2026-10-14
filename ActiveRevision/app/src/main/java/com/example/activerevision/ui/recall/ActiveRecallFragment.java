package com.example.activerevision.ui.recall;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.activerevision.R;
import com.example.activerevision.databinding.FragmentActiveRecallBinding;
import com.example.activerevision.model.RevisionAttempt;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.repository.RevisionRepository;
import com.example.activerevision.util.DateTimeUtils;

public class ActiveRecallFragment extends Fragment {

    private FragmentActiveRecallBinding binding;
    private RevisionRepository repository;

    private long taskId;
    private TaskWithDetails currentTaskDetails;
    private long startTimeMillis;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getLong("taskId", -1);
        }
        startTimeMillis = System.currentTimeMillis();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentActiveRecallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());

        binding.btnRevealNotes.setOnClickListener(v -> {
            binding.cardStudyNotes.setVisibility(View.VISIBLE);
            binding.btnRevealNotes.setVisibility(View.GONE);
        });

        binding.btnSubmitAttempt.setOnClickListener(v -> submitAttempt());

        loadTaskDetails();
    }

    private void loadTaskDetails() {
        if (taskId <= 0) return;

        repository.executeAsync(() -> repository.getTaskWithDetailsById(taskId), taskDetails -> {
            if (binding == null || taskDetails == null) return;
            currentTaskDetails = taskDetails;

            binding.tvRecallInterval.setText("Day " + taskDetails.getTask().getIntervalDays() + " Review");
            binding.tvRecallSubject.setText(taskDetails.getSubjectName());
            binding.tvRecallTopic.setText(taskDetails.getTopicName());
            binding.tvRecallScheduledDate.setText("Scheduled: " + DateTimeUtils.formatDateTime(taskDetails.getTask().getScheduledAt()));

            String notes = !TextUtils.isEmpty(taskDetails.getTopicNotes()) ?
                    taskDetails.getTopicNotes() : taskDetails.getStudySessionNotes();
            if (TextUtils.isEmpty(notes)) {
                notes = "No study notes were recorded for this topic.";
            }
            binding.tvRevealedNotes.setText(notes);
        });
    }

    private void submitAttempt() {
        if (currentTaskDetails == null) return;

        String rating = RevisionAttempt.RATING_EASY;
        int checkedId = binding.rgDifficulty.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_medium) {
            rating = RevisionAttempt.RATING_MEDIUM;
        } else if (checkedId == R.id.rb_hard) {
            rating = RevisionAttempt.RATING_HARD;
        } else if (checkedId == R.id.rb_failed) {
            rating = RevisionAttempt.RATING_FAILED;
        }

        String scoreStr = binding.etRecallScore.getText() != null ?
                binding.etRecallScore.getText().toString().trim() : "";
        Integer score = null;
        if (!TextUtils.isEmpty(scoreStr)) {
            try {
                score = Integer.parseInt(scoreStr);
                if (score < 0 || score > 100) {
                    Toast.makeText(requireContext(), "Score must be between 0 and 100", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Invalid score format", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        boolean noNotes = binding.cbRecalledWithoutNotes.isChecked();
        String mistakes = binding.etMistakes.getText() != null ?
                binding.etMistakes.getText().toString().trim() : "";
        String notesAfter = binding.etNotesAfterReview.getText() != null ?
                binding.etNotesAfterReview.getText().toString().trim() : "";

        int durationSeconds = (int) ((System.currentTimeMillis() - startTimeMillis) / 1000);

        long now = System.currentTimeMillis();
        RevisionAttempt attempt = new RevisionAttempt(
                taskId,
                score,
                rating,
                noNotes,
                durationSeconds,
                mistakes,
                notesAfter,
                now
        );

        binding.btnSubmitAttempt.setEnabled(false);

        repository.executeAsync(() -> repository.completeRevisionAttempt(attempt), success -> {
            if (getContext() == null) return;
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(requireContext(), "Recall session completed! Great job!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).popBackStack();
            } else {
                binding.btnSubmitAttempt.setEnabled(true);
                Toast.makeText(requireContext(), "Failed to save attempt. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
