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
    private long locationAlarmDurrarion;
    private String locationAlarmNote;
    private double startLat;
    private double startLng;
    private Button addLocAlarm;
    private CountDownTimer count;
    private long lad;
    private boolean locationChanged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        locationAlarmTimer.setText("2:00");//default Time
        locationAlarmDurrarion=60000*2;

        addLocAlarm =(Button) findViewById(R.id.AddLocationAlarm);
        addLocAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //popUpWindow
                startLat=location.getLatitude();
                startLng=location.getLongitude();
                lad=locationAlarmDurrarion;
                locationChanged=false;


                //Starting Timer
                count= new CountDownTimer(locationAlarmDurrarion,1000) {
                    @Override
                    public void onTick(long l) {
                        locationAlarmDurrarion=l;
                        updateTimer(locationAlarmDurrarion);
                       if(locationChanged==true){
                            resetTimer();
                        }





                    }

                    @Override
                    public void onFinish() {
                    count.cancel();
                    locationAlarmTimer.setText("2:00");
                    //Alarm Goes Off

                    }
                }.start();

            }

        });







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
        locationAlarmDurrarion=lad;
        updateTimer(locationAlarmDurrarion);
        locationChanged=false;
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
