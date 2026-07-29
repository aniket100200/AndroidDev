package com.example.navdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.NavigableMap;

public class HomeFragment extends Fragment {

    private final static String NAME="name";
    private  final static String DESC="description";


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance(String name,String description){

        HomeFragment homeFragment= new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putString(NAME,name);
        bundle.putString(DESC,description);

        homeFragment.setArguments(bundle);

        return homeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        TextView name= view.findViewById(R.id.homeName);
        TextView des=view.findViewById(R.id.homeDescription);

        try{
            Bundle bundle = getArguments();
            name.setText(bundle.getString(NAME));
            des.setText(bundle.getString(DESC));
        }catch (Exception t){
            name.setText(NAME);
            des.setText(DESC);
            ((MainActivity) getActivity()).callFromFragment();
        }

        return  view;
    }
}