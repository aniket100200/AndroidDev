package com.example.bgservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    MediaPlayer mp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Prevent overlapping audio and resource leaks if started multiple times
        if (mp != null) {
            mp.stop();
            mp.release();
        }

        // Sanity check: MediaPlayer plays on STREAM_MUSIC, which the ringer
        // silence toggle does NOT mute. If this is 0, you'll get silent
        // "successful" playback with no error anywhere.
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "STREAM_MUSIC volume: " + musicVolume + "/" + maxMusicVolume);
        if (musicVolume == 0) {
            Log.w(TAG, "STREAM_MUSIC volume is 0 - raising it so playback is audible");
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusicVolume / 2, 0);
        }

        mp = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);

        // Null check in case the media player fails to initialize
        if (mp != null) {
            mp.setLooping(true);
            mp.start();
            Log.d(TAG, "Playback started");
        } else {
            Log.e(TAG, "MediaPlayer.create() returned null - the ringtone URI failed to resolve/open. Check Logcat above this line for the underlying IOException.");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.stop();
            mp.release(); // CRITICAL: Release native resources
            mp = null;
        }
        super.onDestroy();
    }
}