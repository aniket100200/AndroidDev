package com.example.navdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NotesFragment extends Fragment {



    public NotesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        try{
            Bundle bundle = getArguments();
            String name=bundle.getString("name");
            TextView notes=view.findViewById(R.id.notes);

           TextView descriptionView=view.findViewById(R.id.descNotes);
           descriptionView.setText(bundle.getString("value"));


        }catch (NullPointerException t){

        }
        return view;
    }
}