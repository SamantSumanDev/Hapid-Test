package com.samant.hapidtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    AppCompatButton btnGetStarted;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        } catch (Exception e) {

        }
        sessionManagement = new SessionManagement(getApplicationContext());
        if (sessionManagement.getSplash() != null) {
            Intent i = new Intent(SplashActivity.this, CreateProfileActivity.class);
            startActivity(i);
            finish();
        }


        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });



    }
}