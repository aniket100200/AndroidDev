package com.example.activerevision.ui.dashboard;

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
import com.example.activerevision.databinding.FragmentDashboardBinding;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.repository.RevisionRepository;

import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private FragmentDashboardBinding binding;
    private RevisionRepository repository;
    private TaskAdapter taskAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());
        taskAdapter = new TaskAdapter(this);

        binding.rvDueTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDueTasks.setAdapter(taskAdapter);

        loadDashboardData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        repository.executeAsync(() -> {
            RevisionRepository.DashboardMetrics metrics = repository.getDashboardMetrics();
            List<TaskWithDetails> dueTasks = repository.getDueOrOverdueTasks();
            return new DashboardData(metrics, dueTasks);
        }, data -> {
            if (binding == null) return;

            binding.tvMetricDueToday.setText(String.valueOf(data.metrics.dueTodayCount));
            binding.tvMetricOverdue.setText(String.valueOf(data.metrics.overdueCount));
            binding.tvMetricUpcoming.setText(String.valueOf(data.metrics.upcomingCount));

            if (data.metrics.averageScore != null) {
                binding.tvMetricScore.setText(String.format(Locale.getDefault(), "%.0f%%", data.metrics.averageScore));
            } else {
                binding.tvMetricScore.setText("N/A");
            }

            taskAdapter.setItems(data.dueTasks);

            if (data.dueTasks.isEmpty()) {
                binding.tvEmptyDashboard.setVisibility(View.VISIBLE);
                binding.rvDueTasks.setVisibility(View.GONE);
            } else {
                binding.tvEmptyDashboard.setVisibility(View.GONE);
                binding.rvDueTasks.setVisibility(View.VISIBLE);
            }
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

    private static class DashboardData {
        final RevisionRepository.DashboardMetrics metrics;
        final List<TaskWithDetails> dueTasks;

        DashboardData(RevisionRepository.DashboardMetrics metrics, List<TaskWithDetails> dueTasks) {
            this.metrics = metrics;
            this.dueTasks = dueTasks;
        }
    }
}
