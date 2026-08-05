package com.example.roomlibrary;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.roomlibrary.database.DatabaseHelper;
import com.example.roomlibrary.model.Expense;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle,edtAmount;
    AppCompatButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtTitle=findViewById(R.id.edtName);
        edtAmount=findViewById(R.id.edtAmount);
        addBtn=findViewById(R.id.btnAdd);

        DatabaseHelper dbHelper= DatabaseHelper.getInstance(MainActivity.this);

        addBtn.setOnClickListener((v)->{
            String title= edtTitle.getText().toString();
            String amount=edtAmount.getText().toString();

            if(title==null || title.isBlank()){
                Toast.makeText(MainActivity.this,"Please Enter values and then hit Add button",LENGTH_SHORT).show();
                return;
            }
            dbHelper.expenseDao().addTx(
                    new Expense(title,amount)
            );

           List<Expense> expenses =  dbHelper.expenseDao().getAll();

           for(Expense expense:expenses){
               Log.d("DATA","Title:"+expense.getTitle()+", Amount: "+expense.getAmount());
           }

        });


    }
}