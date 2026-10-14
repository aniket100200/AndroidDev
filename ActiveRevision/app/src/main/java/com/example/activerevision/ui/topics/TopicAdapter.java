package com.example.activerevision.ui.topics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activerevision.databinding.ItemTopicBinding;
import com.example.activerevision.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
        void onEditTopic(Topic topic);
        void onDeleteTopic(Topic topic);
    }

    private final List<TopicItem> items = new ArrayList<>();
    private final OnTopicClickListener listener;

    public static class TopicItem {
        public final Topic topic;
        public final int sessionCount;

        public TopicItem(Topic topic, int sessionCount) {
            this.topic = topic;
            this.sessionCount = sessionCount;
        }
    }

    public TopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<TopicItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopicBinding binding = ItemTopicBinding.inflate(
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
        private final ItemTopicBinding binding;

        ViewHolder(ItemTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TopicItem item, OnTopicClickListener listener) {
            binding.tvTopicName.setText(item.topic.getName());
            if (item.topic.getDescription() != null && !item.topic.getDescription().trim().isEmpty()) {
                binding.tvTopicDescription.setText(item.topic.getDescription());
                binding.tvTopicDescription.setVisibility(View.VISIBLE);
            } else {
                binding.tvTopicDescription.setVisibility(View.GONE);
            }

            binding.tvSessionCount.setText("Studied " + item.sessionCount + (item.sessionCount == 1 ? " time" : " times"));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onTopicClick(item.topic);
            });
            binding.btnEditTopic.setOnClickListener(v -> {
                if (listener != null) listener.onEditTopic(item.topic);
            });
            binding.btnDeleteTopic.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteTopic(item.topic);
            });
        }
    }
}
