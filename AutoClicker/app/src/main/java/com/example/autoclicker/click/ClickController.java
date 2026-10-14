package com.example.autoclicker.click;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.os.Handler;
import android.os.Looper;

import com.example.autoclicker.accessibility.AutoClickAccessibilityService;

public class ClickController {

    public interface ClickCallback {
        void onStateChanged(ClickState state, String message);
        void onProgress(int completed, int total);
    }

    private ClickState state = ClickState.IDLE;
    private ClickConfig config;
    private AutoClickAccessibilityService accessibilityService;
    private ClickCallback callback;
    private int completedCount = 0;

    private final Handler mainHandler;

    public ClickController() {
        this(Looper.getMainLooper() != null ? new Handler(Looper.getMainLooper()) : null);
    }

    public ClickController(Handler handler) {
        this.mainHandler = handler;
    }

    private final Runnable dispatchRunnable = new Runnable() {
        @Override
        public void run() {
            dispatchNextTap();
        }
    };

    public synchronized ClickState getState() {
        return state;
    }

    public synchronized int getCompletedCount() {
        return completedCount;
    }

    public synchronized void start(ClickConfig config, AutoClickAccessibilityService service, ClickCallback callback) {
        if (state == ClickState.RUNNING) {
            if (callback != null) {
                callback.onStateChanged(ClickState.ERROR, "A run is already in progress.");
            }
            return;
        }

        if (service == null) {
            this.state = ClickState.ERROR;
            if (callback != null) {
                callback.onStateChanged(ClickState.ERROR, "Accessibility service is unavailable.");
            }
            return;
        }

        this.config = config;
        this.accessibilityService = service;
        this.callback = callback;
        this.completedCount = 0;
        this.state = ClickState.RUNNING;

        notifyStateChanged(ClickState.RUNNING, "Running — 0 / " + config.getTapCount());
        notifyProgress(0, config.getTapCount());

        dispatchNextTap();
    }

    private synchronized void dispatchNextTap() {
        if (state != ClickState.RUNNING || config == null || accessibilityService == null) {
            return;
        }

        boolean success = accessibilityService.dispatchTap(
                config.getX(),
                config.getY(),
                new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        handleTapCompleted();
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        handleTapCancelled();
                    }
                },
                mainHandler
        );

        if (!success) {
            handleTapFailed();
        }
    }

    private synchronized void handleTapCompleted() {
        if (state != ClickState.RUNNING || config == null) {
            return;
        }

        completedCount++;
        notifyProgress(completedCount, config.getTapCount());

        if (completedCount >= config.getTapCount()) {
            state = ClickState.COMPLETED;
            notifyStateChanged(ClickState.COMPLETED, "Completed — " + completedCount + " / " + config.getTapCount());
        } else {
            notifyStateChanged(ClickState.RUNNING, "Running — " + completedCount + " / " + config.getTapCount());
            if (mainHandler != null) {
                mainHandler.postDelayed(dispatchRunnable, config.getIntervalMs());
            }
        }
    }

    private synchronized void handleTapCancelled() {
        if (state != ClickState.RUNNING) {
            return;
        }
        state = ClickState.ERROR;
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        notifyStateChanged(ClickState.ERROR, "Gesture cancelled by system.");
    }

    private synchronized void handleTapFailed() {
        if (state != ClickState.RUNNING) {
            return;
        }
        state = ClickState.ERROR;
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        notifyStateChanged(ClickState.ERROR, "Failed to dispatch gesture.");
    }

    public synchronized void stop() {
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
        if (state == ClickState.RUNNING) {
            state = ClickState.STOPPED;
            int total = config != null ? config.getTapCount() : 0;
            notifyStateChanged(ClickState.STOPPED, "Stopped — " + completedCount + " / " + total);
        } else {
            state = ClickState.IDLE;
            notifyStateChanged(ClickState.IDLE, "Idle");
        }
    }

    private void notifyStateChanged(final ClickState newState, final String message) {
        if (callback != null) {
            if (mainHandler != null) {
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onStateChanged(newState, message);
                    }
                });
            } else {
                callback.onStateChanged(newState, message);
            }
        }
    }

    private void notifyProgress(final int completed, final int total) {
        if (callback != null) {
            if (mainHandler != null) {
                mainHandler.post(() -> {
                    if (callback != null) {
                        callback.onProgress(completed, total);
                    }
                });
            } else {
                callback.onProgress(completed, total);
            }
        }
    }
}
