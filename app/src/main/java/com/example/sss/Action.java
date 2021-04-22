package com.example.sss;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.SEND_SMS;

public class Action extends AppCompatActivity {

    String number, message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity ma = new MainActivity();

        number = "tel:" + ma.phone;
        message = "Message sent";

        if(hasPermissionGranted(Action.this, SEND_SMS)){
            PendingIntent sentPI;
            String SENT = "SMS_SENT";

            sentPI = PendingIntent.getBroadcast(Action.this, 0,new Intent(SENT), 0);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, sentPI, null);

            Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();


        }
        else{

            ActivityCompat.requestPermissions(Action.this,new String[]{SEND_SMS},102);
        
        }
    }

    private boolean hasPermissionGranted(Context context, String        permission){

        return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {

            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);

                } else {

                    Toast.makeText(this, "Permission Denied..\nMessage Not Sent!!", Toast.LENGTH_SHORT).show();

                }

            case 103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse(number));

                } else {

                    Toast.makeText(this, "Permission Denied..\nPhone Call Not Sent", Toast.LENGTH_SHORT).show();

                }

        }
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
