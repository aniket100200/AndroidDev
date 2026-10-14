package com.example.activerevision.ui.topics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.activerevision.R;
import com.example.activerevision.databinding.FragmentTopicDetailBinding;
import com.example.activerevision.model.Subject;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.model.Topic;
import com.example.activerevision.repository.RevisionRepository;

import com.example.activerevision.ui.dashboard.TaskAdapter;

import java.util.List;

public class TopicDetailFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private FragmentTopicDetailBinding binding;
    private RevisionRepository repository;
    private TaskAdapter taskAdapter;

    private long topicId;
    private long subjectId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicId = getArguments().getLong("topicId", -1);
            subjectId = getArguments().getLong("subjectId", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTopicDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());
        taskAdapter = new TaskAdapter(this);

        binding.rvTopicTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTopicTasks.setAdapter(taskAdapter);

        binding.btnRecordStudy.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putLong("topicId", topicId);
            NavHostFragment.findNavController(this).navigate(R.id.recordStudySessionDialogFragment, args);
        });

        loadTopicDetails();
    }

    private void loadTopicDetails() {
        if (topicId <= 0) return;

        repository.executeAsync(() -> {
            Topic topic = repository.getTopicById(topicId);
            Subject subject = repository.getSubjectById(subjectId);
            List<TaskWithDetails> tasks = repository.getTasksForTopic(topicId);
            return new DetailsData(topic, subject, tasks);
        }, data -> {
            if (binding == null || data.topic == null) return;

            binding.tvDetailSubjectName.setText(data.subject != null ? data.subject.getName() : "Subject");
            binding.tvDetailTopicName.setText(data.topic.getName());

            if (!TextUtils.isEmpty(data.topic.getDescription())) {
                binding.tvDetailTopicDesc.setText(data.topic.getDescription());
                binding.tvDetailTopicDesc.setVisibility(View.VISIBLE);
            } else {
                binding.tvDetailTopicDesc.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.topic.getNotes())) {
                binding.tvDetailNotes.setText(data.topic.getNotes());
            } else {
                binding.tvDetailNotes.setText("No study notes added for this topic.");
            }

            taskAdapter.setItems(data.tasks);
        });
    }

    @Override
    public void onTaskClick(TaskWithDetails taskDetails) {
        Bundle args = new Bundle();
        args.putLong("taskId", taskDetails.getTask().getId());
        NavHostFragment.findNavController(this).navigate(R.id.activeRecallFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class DetailsData {
        final Topic topic;
        final Subject subject;
        final List<TaskWithDetails> tasks;

        DetailsData(Topic topic, Subject subject, List<TaskWithDetails> tasks) {
            this.topic = topic;
            this.subject = subject;
            this.tasks = tasks;
        }
    }
}
