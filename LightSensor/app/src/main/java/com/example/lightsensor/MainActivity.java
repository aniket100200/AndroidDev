package com.example.lightsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager!=null) {
//            Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//            if(lightSensor!=null){
//                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            }
//            Sensor proximitySensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//            if(proximitySensor!=null){
//                sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
//            }

            Sensor acceloSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if(acceloSensor!=null){
                sensorManager.registerListener(this,acceloSensor,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }else{
            Toast.makeText(this,"Sensor is not wokring",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT){
//            ((TextView) findViewById(R.id.txtValue)).setText("Value: "+sensorEvent.values[0]);
        }else if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
//            ((TextView) findViewById(R.id.txtProxy)).setText("Value: "+sensorEvent.values[0]);
//            if(sensorEvent.values[0]>0){
//                    Toast.makeText(this,"Object Is Far",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this,"Object Is Near",Toast.LENGTH_SHORT).show();
//            }
        }else if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            StringBuilder sb=new StringBuilder("X: ");
            sb.append(sensorEvent.values[0]+", Y: ");
            sb.append(sensorEvent.values[1]+", Z: ");
            sb.append(sensorEvent.values[2]);
            ((TextView) findViewById(R.id.txtAccelo)).setText(sb.toString());
        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}