package com.example.autoclicker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.autoclicker.accessibility.AutoClickAccessibilityService;
import com.example.autoclicker.click.ClickConfig;
import com.example.autoclicker.overlay.OverlayService;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextView tvOverlayStatus;
    private TextView tvAccessibilityStatus;
    private Button btnGrantOverlay;
    private Button btnEnableAccessibility;

    private TextInputEditText etTapCount;
    private TextInputEditText etInterval;
    private TextInputEditText etCoordX;
    private TextInputEditText etCoordY;

    private Button btnShowOverlay;
    private Button btnStopAll;

    private int screenWidth = 1080;
    private int screenHeight = 1920;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        calculateScreenDimensions();
        setupListeners();
    }

    private void initViews() {
        tvOverlayStatus = findViewById(R.id.tv_overlay_permission_status);
        tvAccessibilityStatus = findViewById(R.id.tv_accessibility_status);
        btnGrantOverlay = findViewById(R.id.btn_grant_overlay);
        btnEnableAccessibility = findViewById(R.id.btn_enable_accessibility);

        etTapCount = findViewById(R.id.et_tap_count);
        etInterval = findViewById(R.id.et_interval);
        etCoordX = findViewById(R.id.et_coord_x);
        etCoordY = findViewById(R.id.et_coord_y);

        btnShowOverlay = findViewById(R.id.btn_show_overlay);
        btnStopAll = findViewById(R.id.btn_stop_all);
    }

    private void calculateScreenDimensions() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        if (etCoordX != null && etCoordY != null) {
            etCoordX.setText(String.valueOf(screenWidth / 2));
            etCoordY.setText(String.valueOf(screenHeight / 2));
        }
    }

    private void setupListeners() {
        btnGrantOverlay.setOnClickListener(v -> openOverlaySettings());
        btnEnableAccessibility.setOnClickListener(v -> openAccessibilitySettings());
        btnShowOverlay.setOnClickListener(v -> startOverlayService());
        btnStopAll.setOnClickListener(v -> stopAll());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePermissionStatuses();
    }

    private void updatePermissionStatuses() {
        boolean overlayGranted = Settings.canDrawOverlays(this);
        if (overlayGranted) {
            tvOverlayStatus.setText(R.string.overlay_permission_granted);
            tvOverlayStatus.setTextColor(ContextCompat.getColor(this, R.color.green_success));
            btnGrantOverlay.setEnabled(false);
        } else {
            tvOverlayStatus.setText(R.string.overlay_permission_not_granted);
            tvOverlayStatus.setTextColor(ContextCompat.getColor(this, R.color.red_error));
            btnGrantOverlay.setEnabled(true);
        }

        boolean accessibilityConnected = AutoClickAccessibilityService.isConnected();
        if (accessibilityConnected) {
            tvAccessibilityStatus.setText(R.string.accessibility_service_enabled);
            tvAccessibilityStatus.setTextColor(ContextCompat.getColor(this, R.color.green_success));
            btnEnableAccessibility.setEnabled(false);
        } else {
            tvAccessibilityStatus.setText(R.string.accessibility_service_disabled);
            tvAccessibilityStatus.setTextColor(ContextCompat.getColor(this, R.color.red_error));
            btnEnableAccessibility.setEnabled(true);
        }
    }

    private void openOverlaySettings() {
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName())
        );
        startActivity(intent);
    }

    private void openAccessibilitySettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    private void startOverlayService() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Please grant Overlay Permission first.", Toast.LENGTH_LONG).show();
            openOverlaySettings();
            return;
        }

        int tapCount;
        long intervalMs;
        float x;
        float y;

        try {
            String countText = etTapCount.getText() != null ? etTapCount.getText().toString().trim() : "";
            String intervalText = etInterval.getText() != null ? etInterval.getText().toString().trim() : "";
            String xText = etCoordX.getText() != null ? etCoordX.getText().toString().trim() : "";
            String yText = etCoordY.getText() != null ? etCoordY.getText().toString().trim() : "";

            tapCount = Integer.parseInt(countText);
            intervalMs = Long.parseLong(intervalText);
            x = Float.parseFloat(xText);
            y = Float.parseFloat(yText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show();
            return;
        }

        String validationError = ClickConfig.validate(tapCount, intervalMs, x, y, screenWidth, screenHeight);
        if (validationError != null) {
            Toast.makeText(this, validationError, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, OverlayService.class);
        intent.setAction(OverlayService.ACTION_START_OVERLAY);
        intent.putExtra(OverlayService.EXTRA_TAP_COUNT, tapCount);
        intent.putExtra(OverlayService.EXTRA_INTERVAL_MS, intervalMs);
        intent.putExtra(OverlayService.EXTRA_COORD_X, x);
        intent.putExtra(OverlayService.EXTRA_COORD_Y, y);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        Toast.makeText(this, "Floating control started.", Toast.LENGTH_SHORT).show();
    }

    private void stopAll() {
        Intent intent = new Intent(this, OverlayService.class);
        intent.setAction(OverlayService.ACTION_STOP_OVERLAY);
        startService(intent);
        Toast.makeText(this, "All floating controls and auto-clicking stopped.", Toast.LENGTH_SHORT).show();
    }
}
