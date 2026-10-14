package com.example.autoclicker.overlay;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.autoclicker.MainActivity;
import com.example.autoclicker.R;
import com.example.autoclicker.accessibility.AutoClickAccessibilityService;
import com.example.autoclicker.click.ClickConfig;
import com.example.autoclicker.click.ClickController;
import com.example.autoclicker.click.ClickState;

public class OverlayService extends Service implements ClickController.ClickCallback {

    public static final String ACTION_START_OVERLAY = "com.example.autoclicker.ACTION_START_OVERLAY";
    public static final String ACTION_STOP_OVERLAY = "com.example.autoclicker.ACTION_STOP_OVERLAY";
    public static final String ACTION_STOP_CLICKING = "com.example.autoclicker.ACTION_STOP_CLICKING";

    public static final String EXTRA_TAP_COUNT = "extra_tap_count";
    public static final String EXTRA_INTERVAL_MS = "extra_interval_ms";
    public static final String EXTRA_COORD_X = "extra_coord_x";
    public static final String EXTRA_COORD_Y = "extra_coord_y";

    private static final String CHANNEL_ID = "auto_clicker_overlay_channel";
    private static final int NOTIFICATION_ID = 1001;

    private static boolean sIsRunning = false;
    private static OverlayService sInstance;

    private WindowManager windowManager;
    private View overlayView;
    private WindowManager.LayoutParams params;

    private TextView tvInfo;
    private TextView tvStatus;
    private Button btnStart;
    private Button btnStop;
    private ImageButton btnClose;
    private View headerDragArea;

    private ClickController clickController;
    private ClickConfig currentConfig;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    public static boolean isServiceRunning() {
        return sIsRunning;
    }

    public static OverlayService getInstance() {
        return sInstance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sIsRunning = true;
        sInstance = this;
        clickController = new ClickController();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        createNotificationChannel();
        startForegroundServiceNotification();

        initOverlayView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_STOP_OVERLAY.equals(action)) {
                stopSelf();
                return START_NOT_STICKY;
            } else if (ACTION_STOP_CLICKING.equals(action)) {
                if (clickController != null) {
                    clickController.stop();
                }
                return START_STICKY;
            }

            int tapCount = intent.getIntExtra(EXTRA_TAP_COUNT, 20);
            long intervalMs = intent.getLongExtra(EXTRA_INTERVAL_MS, 500);
            float x = intent.getFloatExtra(EXTRA_COORD_X, 500f);
            float y = intent.getFloatExtra(EXTRA_COORD_Y, 1000f);

            currentConfig = new ClickConfig(tapCount, intervalMs, x, y);
            updateOverlayConfigUI();
        }
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Auto Clicker Overlay Service",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Maintains the floating auto-clicker overlay control");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void startForegroundServiceNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.overlay_service_notification_title))
                .setContentText(getString(R.string.overlay_service_notification_text))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            } else {
                startForeground(NOTIFICATION_ID, notification);
            }
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOverlayView() {
        overlayView = LayoutInflater.from(this).inflate(R.layout.view_floating_control, null);

        tvInfo = overlayView.findViewById(R.id.tv_overlay_info);
        tvStatus = overlayView.findViewById(R.id.tv_overlay_status);
        btnStart = overlayView.findViewById(R.id.btn_overlay_start);
        btnStop = overlayView.findViewById(R.id.btn_overlay_stop);
        btnClose = overlayView.findViewById(R.id.btn_overlay_close);
        headerDragArea = overlayView.findViewById(R.id.header_drag_area);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 300;

        windowManager.addView(overlayView, params);

        headerDragArea.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(overlayView, params);
                    return true;
            }
            return false;
        });

        btnStart.setOnClickListener(v -> startClicking());
        btnStop.setOnClickListener(v -> stopClicking());
        btnClose.setOnClickListener(v -> stopSelf());
    }

    private void updateOverlayConfigUI() {
        if (currentConfig != null && tvInfo != null) {
            String text = "Target: (" + (int) currentConfig.getX() + ", " + (int) currentConfig.getY() + ") | " + currentConfig.getTapCount() + " taps";
            tvInfo.setText(text);
        }
    }

    private void startClicking() {
        if (!AutoClickAccessibilityService.isConnected()) {
            tvStatus.setText("Error: Accessibility service disabled");
            Toast.makeText(this, "Please enable the Accessibility Service in Settings first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentConfig == null) {
            currentConfig = new ClickConfig(20, 500, 500f, 1000f);
        }

        clickController.start(currentConfig, AutoClickAccessibilityService.getInstance(), this);
    }

    private void stopClicking() {
        if (clickController != null) {
            clickController.stop();
        }
    }

    public void stopAll() {
        stopClicking();
        stopSelf();
    }

    @Override
    public void onStateChanged(ClickState state, String message) {
        if (tvStatus != null) {
            tvStatus.setText(message);
        }
        if (btnStart != null && btnStop != null) {
            if (state == ClickState.RUNNING) {
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
            } else {
                btnStart.setEnabled(true);
            }
        }
    }

    @Override
    public void onProgress(int completed, int total) {
        if (tvStatus != null) {
            tvStatus.setText("Running — " + completed + " / " + total);
        }
    }

    @Override
    public void onDestroy() {
        sIsRunning = false;
        sInstance = null;
        if (clickController != null) {
            clickController.stop();
        }
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
        super.onDestroy();
    }
}
