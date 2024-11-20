package com.example.flash;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Flash extends AppCompatActivity {

    private boolean isFlashOn = false;
    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        ImageView flashlightIcon = findViewById(R.id.flashlightIcon);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }

        flashlightIcon.setOnClickListener(view -> {
            if (isFlashOn) {
                turnOffFlashlight(flashlightIcon);
            } else {
                turnOnFlashlight(flashlightIcon);
            }
        });
    }

    private void turnOnFlashlight(ImageView icon) {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, true);
                isFlashOn = true;


                icon.setImageResource(R.drawable.flash_on);

                Toast.makeText(this, "Фонарик включен", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не удалось включить фонарик", Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOffFlashlight(ImageView icon) {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId, false);
                isFlashOn = false;


                icon.setImageResource(R.drawable.flash_off);

                Toast.makeText(this, "Фонарик выключен", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Не удалось выключить фонарик", Toast.LENGTH_SHORT).show();
        }
    }
}
