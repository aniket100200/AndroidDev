package com.example.activerevision.ui.topics;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.activerevision.R;
import com.example.activerevision.databinding.DialogAddTopicBinding;
import com.example.activerevision.databinding.FragmentTopicsBinding;
import com.example.activerevision.model.Topic;
import com.example.activerevision.repository.RevisionRepository;

import java.util.ArrayList;
import java.util.List;

public class TopicsFragment extends Fragment implements TopicAdapter.OnTopicClickListener {

    private FragmentTopicsBinding binding;
    private RevisionRepository repository;
    private TopicAdapter adapter;

    private long subjectId;
    private String subjectName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subjectId = getArguments().getLong("subjectId", -1);
            subjectName = getArguments().getString("subjectName", "Topics");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTopicsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());
        adapter = new TopicAdapter(this);

        binding.tvSubjectHeader.setText(subjectName);
        binding.rvTopics.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTopics.setAdapter(adapter);

        binding.fabAddTopic.setOnClickListener(v -> showAddOrEditTopicDialog(null));

        loadTopics();
    }

    private void loadTopics() {
        if (subjectId <= 0) return;
        repository.executeAsync(() -> {
            List<Topic> topics = repository.getTopicsForSubject(subjectId);
            List<TopicAdapter.TopicItem> items = new ArrayList<>();
            for (Topic t : topics) {
                int sessionCount = repository.getTasksForTopic(t.getId()).size() / 5; // 5 tasks per session
                items.add(new TopicAdapter.TopicItem(t, sessionCount));
            }
            return items;
        }, items -> {
            if (binding == null) return;
            adapter.setItems(items);
            if (items.isEmpty()) {
                binding.tvEmptyTopics.setVisibility(View.VISIBLE);
                binding.rvTopics.setVisibility(View.GONE);
            } else {
                binding.tvEmptyTopics.setVisibility(View.GONE);
                binding.rvTopics.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAddOrEditTopicDialog(@Nullable Topic topicToEdit) {
        DialogAddTopicBinding dialogBinding = DialogAddTopicBinding.inflate(getLayoutInflater());
        boolean isEdit = topicToEdit != null;

        if (isEdit) {
            dialogBinding.etTopicName.setText(topicToEdit.getName());
            dialogBinding.etTopicDesc.setText(topicToEdit.getDescription());
            dialogBinding.etTopicNotes.setText(topicToEdit.getNotes());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(isEdit ? "Edit Topic" : "New Topic")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = dialogBinding.etTopicName.getText() != null ?
                            dialogBinding.etTopicName.getText().toString().trim() : "";
                    String desc = dialogBinding.etTopicDesc.getText() != null ?
                            dialogBinding.etTopicDesc.getText().toString().trim() : "";
                    String notes = dialogBinding.etTopicNotes.getText() != null ?
                            dialogBinding.etTopicNotes.getText().toString().trim() : "";

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(requireContext(), "Topic name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    repository.executeAsync(() -> {
                        if (isEdit) {
                            topicToEdit.setName(name);
                            topicToEdit.setDescription(desc);
                            topicToEdit.setNotes(notes);
                            return repository.updateTopic(topicToEdit);
                        } else {
                            Topic newTopic = new Topic(subjectId, name, desc, notes, System.currentTimeMillis());
                            return repository.addTopic(newTopic) > 0;
                        }
                    }, success -> {
                        if (Boolean.TRUE.equals(success)) {
                            loadTopics();
                        } else {
                            Toast.makeText(requireContext(), "Topic with this name already exists in this subject!", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onTopicClick(Topic topic) {
        Bundle args = new Bundle();
        args.putLong("topicId", topic.getId());
        args.putLong("subjectId", subjectId);
        NavHostFragment.findNavController(this).navigate(R.id.topicDetailFragment, args);
    }

    @Override
    public void onEditTopic(Topic topic) {
        showAddOrEditTopicDialog(topic);
    }

    @Override
    public void onDeleteTopic(Topic topic) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Topic")
                .setMessage("Are you sure you want to delete '" + topic.getName() + "'? All study sessions and revision tasks for this topic will also be permanently deleted.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.executeAsync(() -> repository.deleteTopic(topic.getId()), success -> loadTopics());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
