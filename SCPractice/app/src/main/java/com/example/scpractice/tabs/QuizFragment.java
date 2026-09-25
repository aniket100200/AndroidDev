package com.example.scpractice.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.scpractice.R;


public class QuizFragment extends Fragment {

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ListView lvSubjects = view.findViewById(R.id.lvSubjects);

        String[] subjects = {"Mathematics", "Reasoning", "History", "Geography", "Polity", "Economics", "General Science", "Current Affairs"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                subjects
        );

        lvSubjects.setAdapter(adapter);

        lvSubjects.setOnItemClickListener((parent, p, position, id) -> {
            //            Intent iNext = new Intent(this, WebViewActivity.class);
            //            iNext.putExtra("pageId", String.valueOf(position));
            //            startActivity(iNext);
            Toast.makeText(getActivity(), "Under Development", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}