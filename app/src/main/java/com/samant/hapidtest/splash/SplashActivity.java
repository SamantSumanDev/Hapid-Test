package com.samant.hapidtest.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.samant.hapidtest.BaseActivity;
import com.samant.hapidtest.profile.CreateProfileActivity;
import com.samant.hapidtest.R;
import com.samant.hapidtest.login.LoginActivity;
import com.samant.hapidtest.sessionManagement.SessionManagement;

import java.util.Objects;

public class SplashActivity extends BaseActivity {

    AppCompatButton btnGetStarted;
    SessionManagement sessionManagement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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