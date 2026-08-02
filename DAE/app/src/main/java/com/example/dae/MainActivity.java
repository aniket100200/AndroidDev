package com.example.dae;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView listView=findViewById(R.id.listView);

        String URL="https://jsonplaceholder.typicode.com/todos/1/users";

        AndroidNetworking.initialize(MainActivity.this);
        AndroidNetworking.get(URL)
                .setPriority(Priority.HIGH)
                .build().getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray resp) {
                        Log.d("RES",resp.toString());

                        //parsing
                        List<String> names=new ArrayList<>();
                        try{
                            for(int i=0;i<resp.length();i++){
                                JSONObject objResult = resp.getJSONObject(i);
                                String name= objResult.getString("name");
                                names.add(name);
                               }

                            ArrayAdapter<String>arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,names);
                            listView.setAdapter(arrayAdapter);

                        }catch (JSONException t){
                            t.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Log.e("Error",anError.toString());
                    }
                });


    }
}