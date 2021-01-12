package com.example.mylightapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Spider_Plant extends AppCompatActivity  implements SensorEventListener {
    public TextView textView;
    public TextView send_email;
    public TextView notif1,notif_humid;
    public TextView humidity,temperature;
    Button show_button;
    DatabaseReference reff;
    public SensorManager sensorManager;
    public Sensor sensor;

    FirebaseOptions options = new FirebaseOptions.Builder().setApiKey("AIzaSyBJR1eAQmvieQDX6mAuUWcDK8JuyoxoyUI")
            .setApplicationId("1:212614269865:web:128f0dd31ad610edc692b8")
            .setProjectId("datafusion-cc8fc")
            .setStorageBucket("datafusion-cc8fc.appspot.com")
            .setDatabaseUrl("https://datafusion-cc8fc-default-rtdb.firebaseio.com")
            .build();



    public Spider_Plant() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_plant);
        textView = findViewById(R.id.text_light_1);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener((SensorEventListener) Spider_Plant.this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
        humidity = (TextView) findViewById(R.id.humidity);
        temperature = (TextView) findViewById(R.id.temperature);
        notif1 = (TextView)findViewById(R.id.notif1);
        notif_humid = (TextView)findViewById(R.id.notif_humid);
        show_button = (Button)findViewById(R.id.showbutton);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        Python py = Python.getInstance();
        PyObject mod = py.getModule("send_email");

        FirebaseApp.initializeApp(this, options, "secondDatabase");
        FirebaseApp secondApp = FirebaseApp.getInstance("secondDatabase");
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                reff = secondDatabase.getReference().child("FusionData").child("record");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = snapshot.child("Temperature").getValue().toString();
                        String hum = snapshot.child("Humidity").getValue().toString();
                        temperature.setText("Temperature(in °C): " + temp);
                        humidity.setText("Humidity: "+ hum);
                        temperature.setTextColor(Color.parseColor("#4B7A47"));
                        humidity.setTextColor(Color.parseColor("#4B7A47"));
                        if(Integer.parseInt(hum)>40 && Integer.parseInt(hum)<60){
                        notif_humid.setText("Humidity - Optimal");
                        notif_humid.setTextColor(Color.parseColor("#4B0082"));

                        }
                        else {
                        notif_humid.setText("Humidity - Not Optimal");
                        notif_humid.setTextColor(Color.parseColor("#4B0082"));
                        PyObject obj = mod.callAttr("create_email_message");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format1.format(cal.getTime());
        //myRef.setValue(Calendar.getInstance().getTime().toString() + "\n" + event.values[0] );
        textView.setText("\nI am a Spider-plant! \nI can tolerate temperature as low as 2°C, but need 18°C-32°C for good growth and require high light(1000 lux) and moist weather! \n Timestamp:" + time   + "\n    Light(in lux) : "  + event.values[0]);
        textView.setTextColor(Color.parseColor("#4B7A47"));
        if(event.values[0]<1000){
         notif1.setText("Result : Light - Very Less");
         notif1.setTextColor(Color.parseColor("#4B0082"));}
        else{
         notif1.setText("Result : Light - Optimum!");
         notif1.setTextColor(Color.parseColor("#4B0082"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}