package com.example.activerevision.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.activerevision.R;
import com.example.activerevision.databinding.FragmentRevisionHistoryBinding;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.repository.RevisionRepository;

import java.util.List;

public class RevisionHistoryFragment extends Fragment implements HistoryTaskAdapter.OnHistoryTaskClickListener {

    private FragmentRevisionHistoryBinding binding;
    private RevisionRepository repository;
    private HistoryTaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRevisionHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());
        adapter = new HistoryTaskAdapter(this);

        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistory.setAdapter(adapter);

        loadHistory();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        repository.executeAsync(() -> repository.getCompletedTasks(), tasks -> {
            if (binding == null) return;
            adapter.setItems(tasks);

            if (tasks.isEmpty()) {
                binding.tvEmptyHistory.setVisibility(View.VISIBLE);
                binding.rvHistory.setVisibility(View.GONE);
            } else {
                binding.tvEmptyHistory.setVisibility(View.GONE);
                binding.rvHistory.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onHistoryTaskClick(TaskWithDetails taskDetails) {
        Bundle args = new Bundle();
        args.putLong("taskId", taskDetails.getTask().getId());
        NavHostFragment.findNavController(this).navigate(R.id.activeRecallFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
