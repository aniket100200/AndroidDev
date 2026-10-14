package com.example.activerevision.ui.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activerevision.databinding.ItemHistoryTaskBinding;
import com.example.activerevision.model.TaskWithDetails;
import com.example.activerevision.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryTaskAdapter extends RecyclerView.Adapter<HistoryTaskAdapter.ViewHolder> {

    public interface OnHistoryTaskClickListener {
        void onHistoryTaskClick(TaskWithDetails taskDetails);
    }

    private final List<TaskWithDetails> items = new ArrayList<>();
    private final OnHistoryTaskClickListener listener;

    public HistoryTaskAdapter(OnHistoryTaskClickListener listener) {
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
        ItemHistoryTaskBinding binding = ItemHistoryTaskBinding.inflate(
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
        private final ItemHistoryTaskBinding binding;

        ViewHolder(ItemHistoryTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TaskWithDetails item, OnHistoryTaskClickListener listener) {
            binding.tvHistoryInterval.setText("Day " + item.getTask().getIntervalDays());
            binding.tvHistorySubject.setText(item.getSubjectName());
            binding.tvHistoryTopic.setText(item.getTopicName());

            Long completedAt = item.getTask().getCompletedAt();
            if (completedAt != null) {
                binding.tvHistoryCompletedAt.setText("Completed: " + DateTimeUtils.formatDateTime(completedAt));
            } else {
                binding.tvHistoryCompletedAt.setText("Completed");
            }

            binding.tvHistoryScore.setText("Completed ✓");

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onHistoryTaskClick(item);
            });
        }
    }
}
