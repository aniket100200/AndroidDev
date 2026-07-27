package com.example.recyclerviewex;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewex.adapter.RecyclerContactAdapter;
import com.example.recyclerviewex.models.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import kotlinx.coroutines.Delay;

public class MainActivity extends AppCompatActivity {

    ArrayList<Contact>contacts=new ArrayList<>();
    RecyclerView recyclerView;
    FloatingActionButton btnOpenDialog;

    RecyclerContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerContact);
        btnOpenDialog=findViewById(R.id.btnOpenDialog);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        generateContactList();

        adapter=new RecyclerContactAdapter(this,contacts);

        recyclerView.setAdapter(adapter);

        btnOpenDialog.setOnClickListener((v -> {
            Dialog dialog=new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.add_update_lay);
            EditText edtName=dialog.findViewById(R.id.edtName);
            EditText edtNumber=dialog.findViewById(R.id.edtNumber);
            AppCompatButton btnAction=dialog.findViewById(R.id.btnAction);

            btnAction.setOnClickListener((view)->{
                String name=null;
                if(!edtName.getText().toString().equals("")){
                    name=edtName.getText().toString();
                }
                String number=null;

                if(!edtNumber.getText().toString().equals("")){
                    number=edtNumber.getText().toString();
                }

                if(name==null || number==null){
                    Toast.makeText(MainActivity.this,"Please Enter a Valid Name or Number ", Toast.LENGTH_SHORT).show();
                    return;
                }

                contacts.add(new Contact(name,number));
                adapter.notifyItemInserted(contacts.size()-1);

                recyclerView.scrollToPosition(contacts.size()-1);

                dialog.dismiss();

            });

            dialog.show();

        }));





    }


    private void generateContactList(){

        int serialNumber=1;
        Contact contact=new Contact(R.drawable.img_1,"Image "+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);


        contact=new Contact(R.drawable.img_10,"Sam "+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img,"First Image"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_2,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_3,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_4,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_5,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_6,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_7,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_8,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_9,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_11,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_12,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);

        contact=new Contact(R.drawable.img_13,"Image 1"+(++serialNumber), UUID.randomUUID().toString());
        contacts.add(contact);
    }
}