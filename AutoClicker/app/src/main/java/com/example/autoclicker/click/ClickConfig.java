package com.example.autoclicker.click;

public final class ClickConfig {
    public static final int MIN_TAP_COUNT = 1;
    public static final int MAX_TAP_COUNT = 100;
    public static final long MIN_INTERVAL_MS = 150;
    public static final long MAX_INTERVAL_MS = 5000;

    private final int tapCount;
    private final long intervalMs;
    private final float x;
    private final float y;

    public ClickConfig(int tapCount, long intervalMs, float x, float y) {
        this.tapCount = tapCount;
        this.intervalMs = intervalMs;
        this.x = x;
        this.y = y;
    }

    public int getTapCount() {
        return tapCount;
    }

    public long getIntervalMs() {
        return intervalMs;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static String validate(int tapCount, long intervalMs, float x, float y, int screenWidth, int screenHeight) {
        if (tapCount < MIN_TAP_COUNT || tapCount > MAX_TAP_COUNT) {
            return "Tap count must be between " + MIN_TAP_COUNT + " and " + MAX_TAP_COUNT + ".";
        }
        if (intervalMs < MIN_INTERVAL_MS || intervalMs > MAX_INTERVAL_MS) {
            return "Interval must be between " + MIN_INTERVAL_MS + " ms and " + MAX_INTERVAL_MS + " ms.";
        }
        if (x < 0 || (screenWidth > 0 && x > screenWidth)) {
            return "X coordinate must be within screen bounds (0 to " + screenWidth + ").";
        }
        if (y < 0 || (screenHeight > 0 && y > screenHeight)) {
            return "Y coordinate must be within screen bounds (0 to " + screenHeight + ").";
        }
        return null;
    }
}
