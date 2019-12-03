package com.example.alarmclock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.view.View.OnClickListener;

import android.view.View;

public class MainActivity extends AppCompatActivity {
    private TextView locationAlarmTimer;
    private TextView latitude;
    private TextView longitude;
    private LocationManager locationManager;
    private Criteria criteria;
    private MyLocationListener mylistener;
    private Location location;
    private long locationAlarmDurrarion=60000*2;
    private String locationAlarmNote;
    private double startLat;
    private double startLng;
    private Button addLocAlarm;
    private CountDownTimer count;
    private boolean locationChanged;
    private boolean start=false;
    private EditText durration;
    private EditText message;
    private String alarmMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set Recursive PopUp invisible on start
        final LinearLayout popUp = (LinearLayout) findViewById(R.id.LocationAlarmPopUp);
        popUp.setVisibility(View.GONE);


        latitude = (TextView) findViewById(R.id.lat);
        longitude = (TextView) findViewById(R.id.lon);


        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    //default
        mylistener = new MyLocationListener();

        // Check if the app has permission to access location data
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        11);
            }
        } else {
            Toast.makeText(this, "" + Manifest.permission.ACCESS_FINE_LOCATION + " is already granted.", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mylistener);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        // Check if GPS is enabled.
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
                longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        //timer
        locationAlarmTimer= (TextView) findViewById(R.id.locationAlarmTimer);
        locationAlarmTimer.setText("0:00 No Alarm Set");//default Time

            durration = (EditText) findViewById(R.id.Durration);
             durration.setText("2");
             message = (EditText) findViewById(R.id.LocationAlarmmessage);
        addLocAlarm =(Button) findViewById(R.id.AddLocationAlarm);
        addLocAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //popUpWindow
                start = false;
                popUp.setVisibility(View.VISIBLE);
                startLat=location.getLatitude();
                startLng=location.getLongitude();
                locationChanged=false;


                Button locationAlarmDismiss = (Button) findViewById(R.id.LocationAlarmDismiss);
                locationAlarmDismiss.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        start=true;
                        alarmMessage= message.getText().toString();
                        String value= durration.getText().toString();
                        double inputValue= Double.parseDouble(value);
                        locationAlarmDurrarion= (long) inputValue*60000;
                        popUp.setVisibility(View.GONE);

                        //Starting Timer
                        if(start==true) {
                            count = new CountDownTimer(locationAlarmDurrarion, 1000) {
                                @Override
                                public void onTick(long l) {
                                    locationAlarmDurrarion = l;
                                    updateTimer(locationAlarmDurrarion + 1000);
                                    if (locationChanged == true) {
                                        resetTimer();
                                    }


                                }

                                @Override
                                public void onFinish() {
                                    alert(alarmMessage);

                                    count.cancel();
                                    locationChanged = false;
                                    locationAlarmTimer.setText("0:00 No Alarm Set");
                                }

                            }.start();
                        }
                    }
                });






            }

        });







    }
    public void alert(String m){
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setMessage("Get up and Walk!  \n"+ m)
                .setCancelable(false)
                .setPositiveButton("Reset Alarm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      count.cancel();
                      locationChanged=false;
                      count.start();

                    }
                });
        al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

               dialog.cancel();
            }
        });
        AlertDialog alert = al.create();
        alert.show();


    }



    public void updateTimer(long t){
        int minutes= (int) t / 60000;
        int seconds = (int) t % 60000 /1000;

        String currentTime;
        currentTime = ""+minutes;
        currentTime = currentTime +":";

        if(seconds<10) currentTime = currentTime + "0";


            currentTime = currentTime + seconds;

        locationAlarmTimer.setText(currentTime);
    }
    public void resetTimer(){
        count.cancel();
        locationChanged=false;
        count.start();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    {
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No permission !", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields
            latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
            longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            Toast.makeText(MainActivity.this, "Location changed!",
                    Toast.LENGTH_SHORT).show();
            locationChanged=true;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
