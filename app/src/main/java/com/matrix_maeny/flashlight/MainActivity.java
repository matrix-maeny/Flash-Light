package com.matrix_maeny.flashlight;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.matrix_maeny.flashlight.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final Random random = new Random();
    private volatile boolean spin = false;
    private volatile boolean shouldRun = true;
    private Animation animation;
    private CameraManager manager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (CameraManager) getSystemService(CAMERA_SERVICE);

        binding.switchBtn.setOnClickListener(v -> {

            if (binding.switchBtn.getText().equals("ON")) {
                shouldRun = true;
                startRotations();
                setLightState(true);
                binding.switchBtn.setText("OFF");
            } else {
                shouldRun = false;
                binding.switchBtn.setText("ON");
                setLightState(false);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLightState(boolean shouldOn) {
        String camId;

        try {
            camId = manager.getCameraIdList()[0];

            manager.setTorchMode(camId, shouldOn);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "some error occured", Toast.LENGTH_SHORT).show();
        }
    }


    private void startRotations() {

        new Thread() {
            public void run() {
                while (shouldRun) {
                    if (!spin) try {


                        float pX = binding.gearIv.getMeasuredWidth() / 2.0f;
                        float pY = binding.gearIv.getMeasuredHeight() / 2.0f;

                        animation = new RotateAnimation(0, 10000, pX, pY);
                        animation.setDuration(10000);

                        animation.setFillAfter(true);

                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                spin = true;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                spin = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });


                        binding.gearIv.startAnimation(animation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    animation.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // go to about activity

        startActivity(new Intent(MainActivity.this,AboutActivity.class));

        return super.onOptionsItemSelected(item);
    }

}