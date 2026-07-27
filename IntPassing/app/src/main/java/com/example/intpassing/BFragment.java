package com.example.intpassing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BFragment extends Fragment {


    public BFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewB= inflater.inflate(R.layout.fragment_b, container, false);
        TextView txtFrag2=viewB.findViewById(R.id.txtFrag2);

        return  viewB;
    }
}