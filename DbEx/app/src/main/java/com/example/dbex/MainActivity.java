package com.example.dbex;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dbex.model.Contact;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MyDbHelper helper=new MyDbHelper(this);
        listView=findViewById(R.id.myListView);

//        helper.addContact(new Contact("Aniket","+91 93595626454"));
//        helper.addContact(new Contact("Ojas","+91 9359407416"));
//        helper.addContact(new Contact("Aniket","+91 9665178772"));
//        helper.addContact(new Contact("Vedan","+91 9322462271"));
//        helper.addContact(new Contact("Pranay","+91 9096891333"));


//
//        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
//        listView.setAdapter(adapter);
//        Contact updateOjas=new Contact();
//        updateOjas.setId(2);
//        updateOjas.setPhoneNo("9359407416");
//        updateOjas.setName("Ojas");
//        helper.update(updateOjas);

        helper.deleteById(2);
        List<Contact> contacts= helper.fetchContacts();

        ArrayAdapter<Contact> adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,contacts);
        listView.setAdapter(adapter);




    }
}