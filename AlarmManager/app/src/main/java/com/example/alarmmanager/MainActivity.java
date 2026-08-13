package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    static final int ALARM_RQ=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        findViewById(R.id.btnSet).setOnClickListener(v->{

            int time=Integer.parseInt(((EditText)findViewById(R.id.edtText)).getText().toString());
            long triggerTime= System.currentTimeMillis()+time*1000;

            Intent iBroadcast=new Intent(MainActivity.this,MyReceiver.class);

            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,ALARM_RQ,iBroadcast,PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.set(AlarmManager.RTC_WAKEUP,triggerTime,pi);
        });
    }
}