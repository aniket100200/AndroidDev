package com.example.custtoastexample;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnClickMe;
    TextView txtMessage;
    private static int count=0;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
////        toast("Hello Aniket"); //this is Default Toast
//        Log.d("Test","Test Log");
//
//        Toast toast=new Toast(getApplicationContext());
//        View view = getLayoutInflater().inflate(R.layout.custom_toast_layout,(ViewGroup) findViewById(R.id.viewContainer));
//        toast.setView(view);
//
//        if(txtMessage==null)
//             txtMessage = view.findViewById(R.id.txtMessage);
//
//        toast.setDuration(Toast.LENGTH_SHORT);
//
//        btnClickMe=findViewById(R.id.btnClickMe);
//        addBtnListner(toast);
//
//
//        /**
//         * for Alert Dialog Box
//         */
//
//        //Signle Button
//
////        singleDialog();
//
//
//        //Double Dialog for delete

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            Dialog dialog= new Dialog(this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog_layout);

            AppCompatButton btnOkay= dialog.findViewById(R.id.btnOkay);

            btnOkay.setOnClickListener((v)->{
                dialog.dismiss();
            });

            dialog.show();
    }

    ////        deleteDialog();
//
//        //Three Btn Dialog
//        OnBackPressedCallback callback= new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                   new AlertDialog.Builder(MainActivity.this).
//                    setTitle("Exit")
//                            .setIcon(R.drawable.logout_line_icon)
//                            .setMessage("Do you really want to exit?")
//                            .setPositiveButton("No",(di,i)->{
//
//                    }).setNegativeButton("Yes",(di,i)->{
//                        finish();
//                    }).setNeutralButton("Cancel",(di,i)->{
//
//                    }).show();
//            }
//        };
//
//        getOnBackPressedDispatcher().addCallback(callback);
//
//
//
//    }



    private void singleDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Terms and Condition")
                .setIcon(R.drawable.outline_align_flex_end_24)
                .setMessage("Have You read all the Terms and Conditions")
                .setPositiveButton("Proceed and Continue", (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "Yes, You can Proceed now", Toast.LENGTH_SHORT).show();
                }).show();
    }

    private void deleteDialog(){
        AlertDialog.Builder delDialog=new AlertDialog.Builder(this);

        delDialog.setTitle("Delete");
        delDialog.setIcon(R.drawable.recycle_bin_line_icon);

        delDialog.setMessage("Do you want to delete?");

        delDialog.setPositiveButton("Yes",(di,i)->{
            //for Yes
            Toast.makeText(MainActivity.this,"Deleted Succesffully",Toast.LENGTH_SHORT).show();
        });

        delDialog.setNegativeButton("No",(di,i)->{
            //for No
        });

        delDialog.show();
    }

    private void addBtnListner(Toast toast){
        btnClickMe.setOnClickListener((v)->{
            txtMessage.setText("Click count : "+ ++count);
            int position=count%9;
            switch (position){
                case 1:{
                    toast.setGravity(Gravity.TOP|Gravity.LEFT,0,0);
                    singleDialog();
                    break;
                }

                case 2:{
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    deleteDialog();
                    break;
                }

                case 3:{
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.LEFT,0,0);
                    break;
                }

                case  4:{
                    toast.setGravity(Gravity.CENTER,0,0);
                    break;
                }

                case 5:{
                    toast.setGravity(Gravity.BOTTOM|Gravity.RIGHT,0,0);
                    break;
                }

                default:{
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                }


            }
            toast.show();

        });
    }

    private void toast(String message){
        toast(message,false);
    }

    private void toast(String message,boolean isLong){
        Toast toast = Toast.makeText(this,"Hello Aniket",isLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
        toast.show();

    }
}