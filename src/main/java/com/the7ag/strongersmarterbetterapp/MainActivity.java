package com.the7ag.strongersmarterbetterapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {
    private SensorManager sensormaneger;
    private TextInputLayout inputText;
    private TextView reading;
    private Sensor sensor;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reading = findViewById(R.id.textView3);
        inputText =findViewById(R.id.textInputLayout);
        sensormaneger = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensormaneger.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensormaneger.registerListener(proximitySensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_GAME);
    }
    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // method to check accuracy changed in sensor.
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // check if the sensor type is proximity sensor.
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    reading.setText("Proximiy sensor read is 0");
                    // here we are setting our status to our textview..
                    // if sensor event return 0 then object is closed
                    // to sensor else object is away from sensor.
                    String ON="http://"+inputText.getEditText().getText()+"/ON";
                    reading.setText(ON);
                    Request request = new Request.Builder()
                            .url(ON)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            ResponseBody responseBody=response.body();
                            if(response.isSuccessful()){
                                reading.setText("LED OFF");
                            }
                        }
                    });
                } else {
                    reading.setText("Proximiy sensor read is 5");
                    String OFF="http://"+inputText.getEditText().getText()+"/OFF";
                    reading.setText(OFF);
                    Request request = new Request.Builder()
                            .url(OFF)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            ResponseBody responseBody=response.body();
                            if(response.isSuccessful()){
                                reading.setText("LED ON");
                            }
                        }
                    });

                }
            }
        }
    };
}