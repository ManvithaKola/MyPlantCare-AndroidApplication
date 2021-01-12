package com.example.mylightapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;


@IgnoreExtraProperties
class SenseData {

    public String time;
    public float lux_value;

    public SenseData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public SenseData(String time, float lux_value) {
        this.time = time;
        this.lux_value = lux_value;
    }

}

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Button button;
    public TextView textView;
    public SensorManager sensorManager;
    public Sensor sensor;
    final String TAG = "SensorLog";
    public String time = "12:00";
    public float lux_value = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_light);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener((SensorEventListener) MainActivity.this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_spider();
            }
        });


    }

    public void open_spider(){
        Intent intent = new Intent(this, Spider_Plant.class);
        startActivity(intent);

    }

    public void onSensorChanged(SensorEvent event) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time = format1.format(cal.getTime());
                lux_value = event.values[0];
                SenseData sensedata = new SenseData(time, lux_value);
                //myRef.child("SensorInfo").push().setValue(sensedata);
                myRef.child(time).setValue(lux_value);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, new Date(), 400000);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format1.format(cal.getTime());
        //myRef.setValue(Calendar.getInstance().getTime().toString() + "\n" + event.values[0] );
        textView.setText("Time: " + time   + "\n       Light(in lux) : "  + event.values[0]);
        textView.setTextColor(Color.parseColor("#4B7A47"));
        Log.d(TAG, Calendar.getInstance().getTime().toString() + " \n" + event.values[0]);
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}
