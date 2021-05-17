package com.example.cpsystemsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Vibrator;

public class ActivityTrack extends AppCompatActivity implements SensorEventListener {
    protected MyApplication app;
    Vibrator vibrator;
    Sensor acelerometer;
    Sensor gyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        app = (MyApplication) getApplication();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}