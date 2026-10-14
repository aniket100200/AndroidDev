package com.example.activerevision.ui.subjects;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activerevision.databinding.ItemSubjectBinding;
import com.example.activerevision.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    public interface OnSubjectClickListener {
        void onSubjectClick(Subject subject);
        void onEditSubject(Subject subject);
        void onDeleteSubject(Subject subject);
    }

    private final List<SubjectItem> items = new ArrayList<>();
    private final OnSubjectClickListener listener;

    public static class SubjectItem {
        public final Subject subject;
        public final int topicCount;

        public SubjectItem(Subject subject, int topicCount) {
            this.subject = subject;
            this.topicCount = topicCount;
        }
    }

    public SubjectAdapter(OnSubjectClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SubjectItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectBinding binding = ItemSubjectBinding.inflate(
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
        private final ItemSubjectBinding binding;

        ViewHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SubjectItem item, OnSubjectClickListener listener) {
            binding.tvSubjectName.setText(item.subject.getName());
            binding.tvTopicCount.setText(item.topicCount + (item.topicCount == 1 ? " Topic" : " Topics"));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onSubjectClick(item.subject);
            });
            binding.btnEditSubject.setOnClickListener(v -> {
                if (listener != null) listener.onEditSubject(item.subject);
            });
            binding.btnDeleteSubject.setOnClickListener(v -> {
                if (listener != null) listener.onDeleteSubject(item.subject);
            });
        }
    }
}
