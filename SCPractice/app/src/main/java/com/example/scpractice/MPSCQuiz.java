package com.example.scpractice;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MPSCQuiz extends AppCompatActivity {
    private final boolean IS_TO_REPLACE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mpsc_quiz);

        ListView lvSubjects = findViewById(R.id.lvSubjects);

        String[] subjects = {"Mathematics", "Reasoning", "History", "Geography", "Polity", "Economics", "General Science", "Current Affairs"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                subjects
        );

        lvSubjects.setAdapter(adapter);

        lvSubjects.setOnItemClickListener((parent, view, position, id) -> {
            //            Intent iNext = new Intent(this, WebViewActivity.class);
            //            iNext.putExtra("pageId", String.valueOf(position));
            //            startActivity(iNext);
            Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
        });

    }


    public void loadFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        //ft.add(R.id.container, fragment);

        ft.addToBackStack(null);
        ft.commit();

    }
}