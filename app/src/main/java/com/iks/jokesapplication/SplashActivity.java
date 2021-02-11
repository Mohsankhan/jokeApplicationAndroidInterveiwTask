package com.iks.jokesapplication;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.iks.jokesapplication.common.JokesConstants;
import com.iks.jokesapplication.common.LocalStorageController;
import com.iks.jokesapplication.onboarding.RegistrationActivity;


public class SplashActivity extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();
    LocalStorageController localStorageController;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        LocalStorageController.getInstance().setApplicationContext(getApplicationContext());
        mWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadInitialActivity();
            }
        }, 1000);
    }

    private void loadInitialActivity() {
        String applicationState = LocalStorageController.getInstance().getSharedPreferenceString(JokesConstants.APPLICATION_STATE);
        if (applicationState.equalsIgnoreCase(JokesConstants.SUCCESS_FULL_CREATED)) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        } else {
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            finish();
        }
    }
}