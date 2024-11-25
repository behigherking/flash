package com.example.flash;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Compass extends AppCompatActivity implements SensorEventListener {

        private SensorManager sensorManager;
        private Sensor accelerometer;
        private Sensor magnetometer;
        private ImageView arrow;
        private float[] gravity;
    private float[] geomagnetic;
    private float currentAzimuth = 0f;
        private static final float FILTER_FACTOR = 0.9f;
        private TextView azimuthText;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_compass);
             azimuthText = findViewById(R.id.azimuthText);
             arrow = findViewById(R.id.arrow);

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        @Override
        protected void onResume() {
            super.onResume();
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }

        @Override
        protected void onPause() {
            super.onPause();
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomagnetic = event.values;
            }
            if (gravity != null && geomagnetic != null) {
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
                if (success) {
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    float azimuth = (float) Math.toDegrees(orientation[0]);
                    azimuth = (azimuth + 360) % 360;
                    currentAzimuth = FILTER_FACTOR * currentAzimuth + (1 - FILTER_FACTOR) * azimuth;
                    arrow.setRotation(-currentAzimuth);
                    azimuthText.setText( + Math.round(currentAzimuth) + "°C");
                }
            }
        }
    public void startFlashActivity(View view) {
        Intent intent = new Intent(this, Flash.class);
        startActivity(intent);
    }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
