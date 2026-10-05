package com.example.scpractice.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechListener implements TextToSpeech.OnInitListener {

    // Constants for Hindi and Marathi Locales
    public static final Locale HINDI = Locale.forLanguageTag("hi-IN");
    public static final Locale MARATHI = Locale.forLanguageTag("mr-IN");
    private static final String TAG = "TextToSpeechListener";
    private final TextToSpeech textToSpeech;
    private final OnInitCallback callback;
    private Locale currentLocale;
    private boolean isReady = false;
    private Runnable onDoneCallback;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private String pendingText = null;
    private Runnable pendingOnDone = null;
    private int pendingQueueMode = TextToSpeech.QUEUE_FLUSH;

    public TextToSpeechListener(Context context) {
        this(context, MARATHI, null);
    }

    public TextToSpeechListener(Context context, Locale locale) {
        this(context, locale, null);
    }

    public TextToSpeechListener(Context context, OnInitCallback callback) {
        this(context, MARATHI, callback);
    }

    public TextToSpeechListener(Context context, Locale locale, OnInitCallback callback) {
        this.currentLocale = locale != null ? locale : MARATHI;
        this.callback = callback;
        this.textToSpeech = new TextToSpeech(context.getApplicationContext(), this);
        this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
                if (onDoneCallback != null) {
                    mainHandler.post(() -> {
                        if (onDoneCallback != null) {
                            onDoneCallback.run();
                            onDoneCallback = null;
                        }
                    });
                }
            }

            @Override
            public void onError(String utteranceId) {
                onDoneCallback = null;
            }
        });
    }

    /**
     * Opens the device's Text-to-Speech settings screen directly.
     * Useful for guiding users on Vivo/Oppo/Xiaomi devices to set Google Speech Services.
     */
    public static void openTtsSettings(Context context) {
        try {
            android.content.Intent intent = new android.content.Intent("com.android.settings.TTS_SETTINGS");
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Unable to open TTS Settings", e);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            applyLanguage(currentLocale);
        } else {
            Log.e(TAG, "TTS Initialization Failed!");
            if (callback != null) {
                callback.onError("Initialization Failed");
            }
        }
    }

    public boolean setLanguage(Locale locale) {
        this.currentLocale = locale;
        if (textToSpeech != null && isReady) {
            return applyLanguage(locale);
        }
        return false;
    }

    private boolean applyLanguage(Locale locale) {
        int result = textToSpeech.setLanguage(locale);

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "Language " + locale.getDisplayName() + " is missing data or not supported.");

            // If Marathi is missing, try Hindi as a fallback
            if ("mr".equals(locale.getLanguage())) {
                Log.w(TAG, "Marathi voice missing. Trying Hindi fallback...");
                int fallbackResult = textToSpeech.setLanguage(HINDI);
                if (fallbackResult != TextToSpeech.LANG_MISSING_DATA && fallbackResult != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isReady = true;
                    if (callback != null) callback.onSuccess();
                    flushPendingSpeech();
                    return true;
                }
            }

            if (callback != null) {
                callback.onError("Language data missing or not supported");
            }
            return false;
        } else {
            isReady = true;
            Log.d(TAG, "TTS Language set successfully: " + locale.getDisplayName());
            if (callback != null) {
                callback.onSuccess();
            }
            flushPendingSpeech();
            return true;
        }
    }

    private void flushPendingSpeech() {
        if (pendingText != null) {
            String text = pendingText;
            Runnable done = pendingOnDone;
            int mode = pendingQueueMode;

            pendingText = null;
            pendingOnDone = null;

            speak(text, mode, done);
        }
    }

    public void speak(String text) {
        speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void speak(String text, Runnable onDone) {
        speak(text, TextToSpeech.QUEUE_FLUSH, onDone);
    }

    public void speak(String text, int queueMode) {
        speak(text, queueMode, null);
    }

    public void speak(String text, int queueMode, Runnable onDone) {
        if (isReady && textToSpeech != null) {
            this.onDoneCallback = onDone;
            textToSpeech.speak(text, queueMode, null, "TTS_ID_" + System.currentTimeMillis());
        } else {
            Log.w(TAG, "TTS is not ready yet. Queuing message.");
            this.pendingText = text;
            this.pendingQueueMode = queueMode;
            this.pendingOnDone = onDone;
        }
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public interface OnInitCallback {
        void onSuccess();

        void onError(String message);
    }


}
