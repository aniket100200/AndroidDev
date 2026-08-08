package com.example.audioplayer;

import static com.google.android.material.color.MaterialColors.getColor;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.audioplayer.utils.SongData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongList extends Fragment {

    ListView listView;
    Context context;
    List<SongData.Song>songList;
    SongPlayAction songPlayAction;


    public SongList() {
        // Required empty public constructor
        songList=SongData.ALL_SONGS;
        songPlayAction= SongPlayAction.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_song_list, container, false);
        songPlayAction=SongPlayAction.getInstance();
        if(songPlayAction==null){
            return view;
        }

        listView=view.findViewById(R.id.songList);
        context = view.getContext();

        List<String>songTitles= songList.stream().map(SongData.Song::getTitle).collect(Collectors.toList());
        ArrayAdapter<String>adapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,songTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView,myView,position,x)->{
            songPlayAction.setSong(position);
        });


        return  view;
    }
}