package com.example.audioplayer;

public enum SongCache {
    CACHE_CURRENT_SONG,
    CURRENT_SONG;

    static String getVal(SongCache cache){
        if(cache==CACHE_CURRENT_SONG)return "current_Song_cache";
        return "current_song";

    }


}
