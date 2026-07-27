package com.example.bmi;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView txtResult;
        EditText edtWeight,edtHeightFt,edtHeightIn;
        AppCompatButton btnCalculate;

        edtWeight=findViewById(R.id.edtWeight);
        edtHeightFt=findViewById(R.id.edtHeightFt);
        edtHeightIn=findViewById(R.id.edtHeight);
        btnCalculate=findViewById(R.id.btnCalculate);
        txtResult=findViewById(R.id.txtResult);

        LinearLayout llMain=findViewById(R.id.llMain);

        btnCalculate.setOnClickListener((view)->{
            try{
                Integer weight = Integer.parseInt(edtWeight.getText().toString());
                Integer heightInFeet = Integer.parseInt(edtHeightFt.getText().toString());
                Integer heightInIn = Integer.parseInt(edtHeightIn.getText().toString());
                Integer totalInches = heightInFeet * 12 + heightInIn;
                Double totalCms = totalInches * 2.53;

                Double totalM = totalCms / 100;
                double BMI = weight / Math.pow(totalM, 2);
                String result = "";

                if (BMI > 25) {
                    //Over Weigh
                    result = "You are OverWeight";
                    llMain.setBackgroundColor(getResources().getColor(R.color.overWt, null));

                } else if (BMI < 18) {
                    // Under Weight
                    result = "You are Under Weight";
                    llMain.setBackgroundColor(getResources().getColor(R.color.underWt, null));

                } else {
                    // Healthy
                    result = "You are Healthy";
                    llMain.setBackgroundColor(getResources().getColor(R.color.healthy, null));

                }
                txtResult.setText(result.toCharArray(), 0, result.length());
            }catch (Exception t){
                txtResult.setText(t.getMessage().toCharArray(),0,t.getMessage().length());
            }
        });






    }
}