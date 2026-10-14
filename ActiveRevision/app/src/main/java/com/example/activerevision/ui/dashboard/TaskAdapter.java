package com.example.activerevision.ui.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activerevision.databinding.ItemTaskBinding;
import com.example.activerevision.model.RevisionTask;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(TaskWithDetails taskDetails);
    }

    private final List<TaskWithDetails> items = new ArrayList<>();
    private final OnTaskClickListener listener;

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<TaskWithDetails> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        ViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TaskWithDetails item, OnTaskClickListener listener) {
            RevisionTask task = item.getTask();

            binding.tvIntervalBadge.setText("Day " + task.getIntervalDays());
            binding.tvSubjectName.setText(item.getSubjectName());
            binding.tvTopicName.setText(item.getTopicName());
            binding.tvScheduledAt.setText("Due: " + DateTimeUtils.formatDateTime(task.getScheduledAt()));

            long now = System.currentTimeMillis();
            boolean isOverdue = task.getScheduledAt() < now &&
                    (RevisionTask.STATUS_PENDING.equals(task.getStatus()) || RevisionTask.STATUS_IN_PROGRESS.equals(task.getStatus()));

            if (RevisionTask.STATUS_COMPLETED.equals(task.getStatus())) {
                binding.tvStatusBadge.setText("COMPLETED");
                binding.tvStatusBadge.setTextColor(Color.parseColor("#2E7D32"));
                binding.btnAction.setText("View History");
            } else if (RevisionTask.STATUS_CANCELLED.equals(task.getStatus())) {
                binding.tvStatusBadge.setText("CANCELLED");
                binding.tvStatusBadge.setTextColor(Color.GRAY);
                binding.btnAction.setVisibility(View.GONE);
            } else if (RevisionTask.STATUS_MISSED.equals(task.getStatus()) || isOverdue) {
                binding.tvStatusBadge.setText("OVERDUE");
                binding.tvStatusBadge.setTextColor(Color.parseColor("#C62828"));
                binding.btnAction.setText("Start Recall");
                binding.btnAction.setVisibility(View.VISIBLE);
            } else {
                binding.tvStatusBadge.setText(task.getStatus());
                binding.tvStatusBadge.setTextColor(Color.parseColor("#0277BD"));
                binding.btnAction.setText("Start Recall");
                binding.btnAction.setVisibility(View.VISIBLE);
            }

            binding.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onTaskClick(item);
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onTaskClick(item);
            });
        }
    }
}
