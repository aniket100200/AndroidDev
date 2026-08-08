package com.example.audioplayer;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.audioplayer.utils.SongData;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

public class SongPlayAction {
    MaterialButton btnPrevious, btnPlay, btnNext;
    ProgressBar progressLoader; // Add this

    MediaPlayer mp;
    List<SongData.Song> songList;
    int currentSong = 0;

    TextView songTitle;

    Context context;

    public SongPlayAction(Context context, MaterialButton btnPrevious, MaterialButton btnPlay, MaterialButton btnNext, ProgressBar progressLoader, MediaPlayer mp, List<SongData.Song> songList, int currentSong, TextView songTitle) {
        this.btnPrevious = btnPrevious;
        this.btnPlay = btnPlay;
        this.btnNext = btnNext;
        this.progressLoader = progressLoader;
        this.mp = mp;
        this.songList = songList;
        this.currentSong = currentSong;
        this.songTitle = songTitle;
        this.context = context;
    }



    static SongPlayAction songPlayAction;

    public static SongPlayAction getInstance (Context context, MaterialButton btnPrevious, MaterialButton btnPlay, MaterialButton btnNext, ProgressBar progressLoader, MediaPlayer mp, List<SongData.Song> songList, int currentSong, TextView songTitle){
        if(songPlayAction==null){
            songPlayAction = new SongPlayAction(context,btnPrevious,btnPlay,btnNext,progressLoader,mp,songList,currentSong,songTitle);
        }

        return songPlayAction;

    }

    public static SongPlayAction getInstance(){
        return  songPlayAction;
    }

    public void setSong(){
        setSong(currentSong);
    }

    public void setSong(int currentSong) {
        this.currentSong = currentSong;
        //cache the song
        SharedPreferences pref = context.getSharedPreferences(SongCache.getVal(SongCache.CACHE_CURRENT_SONG),MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();

        editor.putInt(SongCache.getVal(SongCache.CURRENT_SONG),currentSong);
        editor.apply();

        // --- SHOW LOADER STATE ---
        btnPlay.setEnabled(false); // Disable button clicks
        btnPlay.setIconResource(0); // Remove the play/pause icon entirely (0 clears it)
        progressLoader.setVisibility(View.VISIBLE); // Show the white spinner

        SongData.Song song = songList.get(currentSong);
        String onlineAudioPath = getDirectDriveLink(song.getLink());
        String songTitleStr = song.getTitle();

        this.songTitle.setText(songTitleStr);
        Uri onlineUri = Uri.parse(onlineAudioPath);

        try {
            mp.reset();
            mp.setDataSource(context, onlineUri);
            mp.prepareAsync();
        } catch (IOException t) {
            t.printStackTrace();
            // If it fails, hide loader and show an error state/icon here if desired
            progressLoader.setVisibility(View.GONE);
            btnPlay.setIconResource(R.drawable.play_icon);
        }
    }


    private String getDirectDriveLink(String originalLink) {
        if (originalLink != null && originalLink.contains("drive.google.com/file/d/")) {
            try {
                int startIndex = originalLink.indexOf("/d/") + 3;
                int endIndex = originalLink.indexOf("/view");
                String fileId = originalLink.substring(startIndex, endIndex);
                return "https://docs.google.com/uc?export=download&id=" + fileId;
            } catch (Exception e) {
                return originalLink;
            }
        }
        return originalLink;
    }
}
