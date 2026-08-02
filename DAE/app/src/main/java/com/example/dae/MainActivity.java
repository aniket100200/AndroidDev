package com.example.dae;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  final String POST_URL="https://fakestoreapi.com/products";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Create your JSONObject
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", "899620244355");
            jsonBody.put("title", "Brand New Day");
            jsonBody.put("Price", 0.1);
            jsonBody.put("description","this movie we feature Spiderman new story to setting up the Spider-man");
            jsonBody.put("category","Adhar");
            jsonBody.put("image","https://www.google.com/imgres?q=krishna&imgurl=https%3A%2F%2Fm.media-amazon.com%2Fimages%2FI%2F51YrcSFYDRL._AC_UF894%2C1000_QL80_.jpg&imgrefurl=https%3A%2F%2Fwww.amazon.in%2FSamriddhi-Krishna-Wallpapers-Waterproof-Decoration%2Fdp%2FB0D921SF39&docid=lwYYdOyCLEhOuM&tbnid=iFI1ikaAGdPjiM&vet=12ahUKEwjy0beSjIGWAxWRjeEIHeXdCR0QnPAOegQIORAA..i&w=848&h=1000&hcb=2&ved=2ahUKEwjy0beSjIGWAxWRjeEIHeXdCR0QnPAOegQIORAA");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView title=findViewById(R.id.title);
        ImageView imageView=findViewById(R.id.dpImage);



        AndroidNetworking.initialize(this);
        AndroidNetworking.post(POST_URL)
                .addJSONObjectBody(jsonBody)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject resp) {
                        try{
                            title.setText(resp.getString("title"));
                            Uri uri= Uri.parse(resp.getString("image"));
                            imageView.setImageURI(uri);
                        }catch (JSONException t){

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error", "onError: ", anError);
                    }
                });


    }
}