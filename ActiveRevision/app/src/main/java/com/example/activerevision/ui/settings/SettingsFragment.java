package com.example.activerevision.ui.settings;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.activerevision.R;
import com.example.activerevision.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences prefs;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                updateNotificationPermissionUI();
                if (isGranted) {
                    Toast.makeText(requireContext(), "Notification permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Notification permission denied.", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireContext().getSharedPreferences("active_revision_prefs", Context.MODE_PRIVATE);

        boolean notifEnabled = prefs.getBoolean("notifications_enabled", true);
        binding.switchNotifications.setChecked(notifEnabled);

        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        long gracePeriodHours = prefs.getLong("grace_period_hours", 24L);
        if (gracePeriodHours == 12L) {
            binding.rb12h.setChecked(true);
        } else if (gracePeriodHours == 48L) {
            binding.rb48h.setChecked(true);
        } else {
            binding.rb24h.setChecked(true);
        }

        binding.rgGracePeriod.setOnCheckedChangeListener((group, checkedId) -> {
            long hours = 24L;
            if (checkedId == R.id.rb_12h) {
                hours = 12L;
            } else if (checkedId == R.id.rb_48h) {
                hours = 48L;
            }
            prefs.edit().putLong("grace_period_hours", hours).apply();
        });

        binding.btnRequestPermission.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        });

        updateNotificationPermissionUI();
    }

    private void updateNotificationPermissionUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean hasPermission = ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
            if (hasPermission) {
                binding.tvPermissionStatus.setText("Status: Notification permission GRANTED.");
                binding.btnRequestPermission.setVisibility(View.GONE);
            } else {
                binding.tvPermissionStatus.setText("Status: Notification permission DENIED.");
                binding.btnRequestPermission.setVisibility(View.VISIBLE);
            }
        } else {
            binding.tvPermissionStatus.setText("Status: Notification permission granted by system (Pre-Android 13).");
            binding.btnRequestPermission.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
