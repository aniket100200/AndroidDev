package com.example.camera;

import android.app.ComponentCaller;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA_REQ_CODE=1;
    private final int GALLERY_REQ_CODE=100;

    ImageView img;
    AppCompatButton btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        img=findViewById(R.id.imgCamera);
//        btnCamera=findViewById(R.id.btnCamera);
//
//        btnCamera.setOnClickListener((v)->{
//            Intent iCamera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            iCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(iCamera,CAMERA_REQ_CODE);
//
//        });

        /**
         * for Gallery
         */

        img=findViewById(R.id.imgCamera);
        btnCamera=findViewById(R.id.btnCamera);
        btnCamera.setText("Open Gallery");

        btnCamera.setOnClickListener((v)->{
            Intent iGallery= new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery,GALLERY_REQ_CODE);

        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);

//        if(resultCode==RESULT_OK){
//            if(requestCode==CAMERA_REQ_CODE){
//                //for camera
//
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                img.setImageBitmap(image);
//
//            }
//        }

        /***
         * for Gallery
         */


        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                img.setImageURI(data.getData());
            }
        }
    }
}