package com.example.activerevision.ui.subjects;

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
import com.example.activerevision.databinding.DialogAddSubjectBinding;
import com.example.activerevision.databinding.FragmentSubjectsBinding;
import com.example.activerevision.model.Subject;
import com.example.activerevision.repository.RevisionRepository;

import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment extends Fragment implements SubjectAdapter.OnSubjectClickListener {

    private FragmentSubjectsBinding binding;
    private RevisionRepository repository;
    private SubjectAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());
        adapter = new SubjectAdapter(this);

        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSubjects.setAdapter(adapter);

        binding.fabAddSubject.setOnClickListener(v -> showAddOrEditSubjectDialog(null));

        loadSubjects();
    }

    private void loadSubjects() {
        repository.executeAsync(() -> {
            List<Subject> subjects = repository.getAllSubjects();
            List<SubjectAdapter.SubjectItem> items = new ArrayList<>();
            for (Subject s : subjects) {
                int count = repository.getTopicsForSubject(s.getId()).size();
                items.add(new SubjectAdapter.SubjectItem(s, count));
            }
            return items;
        }, items -> {
            if (binding == null) return;
            adapter.setItems(items);
            if (items.isEmpty()) {
                binding.tvEmptySubjects.setVisibility(View.VISIBLE);
                binding.rvSubjects.setVisibility(View.GONE);
            } else {
                binding.tvEmptySubjects.setVisibility(View.GONE);
                binding.rvSubjects.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAddOrEditSubjectDialog(@Nullable Subject subjectToEdit) {
        DialogAddSubjectBinding dialogBinding = DialogAddSubjectBinding.inflate(getLayoutInflater());
        boolean isEdit = subjectToEdit != null;

        if (isEdit) {
            dialogBinding.etSubjectName.setText(subjectToEdit.getName());
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(isEdit ? "Edit Subject" : "New Subject")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = dialogBinding.etSubjectName.getText() != null ?
                            dialogBinding.etSubjectName.getText().toString().trim() : "";
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(requireContext(), "Subject name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    repository.executeAsync(() -> {
                        if (isEdit) {
                            subjectToEdit.setName(name);
                            return repository.updateSubject(subjectToEdit);
                        } else {
                            Subject newSubject = new Subject(name, System.currentTimeMillis());
                            return repository.addSubject(newSubject) > 0;
                        }
                    }, success -> {
                        if (Boolean.TRUE.equals(success)) {
                            loadSubjects();
                        } else {
                            Toast.makeText(requireContext(), "Subject name already exists!", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onSubjectClick(Subject subject) {
        Bundle args = new Bundle();
        args.putLong("subjectId", subject.getId());
        args.putString("subjectName", subject.getName());
        NavHostFragment.findNavController(this).navigate(R.id.topicsFragment, args);
    }

    @Override
    public void onEditSubject(Subject subject) {
        showAddOrEditSubjectDialog(subject);
    }

    @Override
    public void onDeleteSubject(Subject subject) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete '" + subject.getName() + "'? All topics, study sessions, and revision history under this subject will also be permanently deleted.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.executeAsync(() -> repository.deleteSubject(subject.getId()), success -> loadSubjects()) ;
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
