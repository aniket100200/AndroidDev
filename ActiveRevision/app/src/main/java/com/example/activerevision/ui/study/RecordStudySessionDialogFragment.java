package com.example.activerevision.ui.study;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.activerevision.databinding.DialogRecordStudySessionBinding;
import com.example.activerevision.repository.RevisionRepository;
import com.example.activerevision.scheduler.ReminderWorkManager;
import com.example.activerevision.util.DateTimeUtils;

import java.util.Calendar;

public class RecordStudySessionDialogFragment extends DialogFragment {

    private DialogRecordStudySessionBinding binding;
    private RevisionRepository repository;

    private long topicId;
    private Calendar selectedCalendar = Calendar.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicId = getArguments().getLong("topicId", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogRecordStudySessionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = RevisionRepository.getInstance(requireContext());

        updateDateTimeLabel();

        binding.btnChangeDatetime.setOnClickListener(v -> showDateTimePicker());
        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnSaveSession.setOnClickListener(v -> saveSessionAndSchedule());
    }

    private void updateDateTimeLabel() {
        binding.tvSelectedDatetime.setText(DateTimeUtils.formatDateTime(selectedCalendar.getTimeInMillis()));
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedCalendar.set(Calendar.MINUTE, minute);
                                selectedCalendar.set(Calendar.SECOND, 0);
                                updateDateTimeLabel();
                            },
                            selectedCalendar.get(Calendar.HOUR_OF_DAY),
                            selectedCalendar.get(Calendar.MINUTE),
                            false
                    );
                    timePickerDialog.show();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveSessionAndSchedule() {
        if (topicId <= 0) {
            Toast.makeText(requireContext(), "Invalid topic", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        String durationStr = binding.etDuration.getText() != null ?
                binding.etDuration.getText().toString().trim() : "0";
        int durationMinutes = 0;
        if (!TextUtils.isEmpty(durationStr)) {
            try {
                durationMinutes = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Please enter a valid duration number", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (durationMinutes < 0) {
            Toast.makeText(requireContext(), "Duration cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        String notes = binding.etSessionNotes.getText() != null ?
                binding.etSessionNotes.getText().toString().trim() : "";

        // Prevent duplicate submissions
        binding.btnSaveSession.setEnabled(false);
        binding.btnCancel.setEnabled(false);

        long studiedAt = selectedCalendar.getTimeInMillis();
        int finalDurationMinutes = durationMinutes;

        repository.executeAsync(() -> {
            return repository.saveStudySessionAndGenerateSchedule(topicId, studiedAt, finalDurationMinutes, notes);
        }, sessionId -> {
            if (getContext() == null) return;

            if (sessionId > 0) {
                ReminderWorkManager.triggerImmediateCheck(requireContext());
                Toast.makeText(requireContext(), "Study session recorded! 5 revision tasks scheduled.", Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                binding.btnSaveSession.setEnabled(true);
                binding.btnCancel.setEnabled(true);
                Toast.makeText(requireContext(), "Failed to save study session.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
