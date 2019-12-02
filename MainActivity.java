package com.example.androidalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Recursive PopUp invisible on start
        final LinearLayout popUp = (LinearLayout) findViewById(R.id.RecursiveAddPopUp);
        popUp.setVisibility(View.GONE);

        //set alarm notification invisible on start
        final LinearLayout alarmNotification = (LinearLayout) findViewById(R.id.AlarmPopUp);
        alarmNotification.setVisibility(View.GONE);

        //Create variables for data fields in recursive alarm
        final TextInputEditText alarmMessage = (TextInputEditText) findViewById(R.id.RecursiveAlarmMessage);
        final TextInputEditText hour = (TextInputEditText) findViewById(R.id.RecursiveAlarmHours);
        final TextInputEditText minute = (TextInputEditText) findViewById(R.id.RecursiveAlarmMinutes);
        final TextInputEditText seconds = (TextInputEditText) findViewById(R.id.RecursiveAlarmSeconds);
        final CheckBox mon = (CheckBox) findViewById(R.id.checkBoxM);
        final CheckBox tue = (CheckBox) findViewById(R.id.checkBoxTu);
        final CheckBox wed = (CheckBox) findViewById(R.id.checkBoxW);
        final CheckBox thu = (CheckBox) findViewById(R.id.checkBoxTh);
        final CheckBox fri = (CheckBox) findViewById(R.id.checkBoxF);
        final CheckBox sat = (CheckBox) findViewById(R.id.checkBoxSat);
        final CheckBox sun = (CheckBox) findViewById(R.id.checkBoxSun);
        final TextView messageString = (TextView) findViewById(R.id.AlarmMessageDisplay);
        final TextView locationString = (TextView) findViewById(R.id.LocationAlarmTextBox);

        //Add Alarm recursive
        Button recursiveAdd =(Button) findViewById(R.id.AddRecursiveAlarmButton);
        recursiveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all fields to empty
                alarmMessage.setText("");
                hour.setText("");
                minute.setText("");
                seconds.setText("");
                mon.setChecked(false);
                tue.setChecked(false);
                wed.setChecked(false);
                thu.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);

                popUp.setVisibility(View.VISIBLE);
            }
        });
        //Add
        Button add =(Button) findViewById(R.id.RecursiveAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store data in variables


                //close popup when done
                popUp.setVisibility(View.GONE);
            }
        });

        //Cancel - closes popup when cancel button is clicked
        Button cancel =(Button) findViewById(R.id.RecursiveCancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popUp.setVisibility(View.GONE);
            }
        });

    }

}
