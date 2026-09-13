package com.example.smssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle=intent.getExtras();
        SmsManager smsManager= SmsManager.getDefault();
        smsManager.sendTextMessage("+918806122161",null,"Hello",null,null);

        try{
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object sms : smsObj) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) sms);

                String mobileNumber = message.getDisplayOriginatingAddress();
                String msg = message.getDisplayMessageBody();

                Log.d("Message Details: ", "Mob No: " + mobileNumber + ", Message: " + msg);

            }
        }catch (Exception t){
            t.printStackTrace();
            Log.d("TAG17", "onReceive: ");
        }


    }
}
