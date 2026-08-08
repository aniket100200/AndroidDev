package com.example.audioplayer;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.audioplayer.utils.SongData;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Player} factory method to
 * create an instance of this fragment.
 */
public class Player extends Fragment {

    MaterialButton btnPrevious, btnPlay, btnNext;
    ProgressBar progressLoader; // Add this
    private final MediaPlayer mp = new MediaPlayer();
    List<SongData.Song> songList;
    int currentSong = 0;

    TextView songTitle;

    private static Player player;

    SongPlayAction songPlayAction;


    public Player() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_player, container, false);

        Context context=view.getContext();

            SharedPreferences pref = context.getSharedPreferences(SongCache.getVal(SongCache.CACHE_CURRENT_SONG), MODE_PRIVATE);

            currentSong=pref.getInt(SongCache.getVal(SongCache.CURRENT_SONG),0);

            btnPrevious = view.findViewById(R.id.btnPause);
            btnPlay =  view.findViewById(R.id.btnPlay);
            btnNext =  view.findViewById(R.id.btnStop);
            songTitle =  view.findViewById(R.id.txtTitle);
            progressLoader =  view.findViewById(R.id.progressLoader); // Initialize this

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            songList = SongData.ALL_SONGS;

            songPlayAction=SongPlayAction.getInstance(context,btnPrevious,btnPlay,btnNext,progressLoader,mp,songList,currentSong,songTitle);

            // Load the initial song
            songPlayAction.setSong();

            // 3. When audio finishes buffering: hide loader, show play icon
            mp.setOnPreparedListener(player -> {
                progressLoader.setVisibility(View.GONE); // Hide spinner
                btnPlay.setIconResource(R.drawable.play_icon); // Show play icon
                btnPlay.setEnabled(true);

                // Optional: If you want the song to automatically start playing when they hit Next/Prev
                // uncomment the next two lines:
                // mp.start();
                // btnPlay.setIconResource(R.drawable.pause_icon);
            });

            // If loading/playback fails (e.g. bad stream), stop the spinner and let the user retry.
            mp.setOnErrorListener((player, what, extra) -> {
                progressLoader.setVisibility(View.GONE);
                btnPlay.setIconResource(R.drawable.play_icon);
                btnPlay.setEnabled(true);
                return true;
            });

            btnPlay.setOnClickListener(v -> {
                if (!mp.isPlaying()) {
                    mp.start();
                    btnPlay.setIconResource(R.drawable.pause_icon);
                } else {
                    mp.pause();
                    btnPlay.setIconResource(R.drawable.play_icon);
                }
            });

            btnPrevious.setOnClickListener(v -> {
                if (songPlayAction.currentSong > 0) {
                    songPlayAction.setSong(songPlayAction.currentSong - 1);
                }
            });

            btnNext.setOnClickListener(v -> {
                int next = (songPlayAction.currentSong + 1) % songList.size(); // Loops back to 0 if at the end
                songPlayAction.setSong(next);
            });

            mp.setOnCompletionListener(player -> {
                player.seekTo(0);
                btnPlay.setIconResource(R.drawable.play_icon); // Reset icon when song ends
            });






        return view;
    }


}