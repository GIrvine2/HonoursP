package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * LoadingScreen activity implements the EasySplashScreen to create a loading screen for the app.
 * Uses the App logo as the main image for the loading screen
 */
public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(LoadingScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.BLUE)
                .withLogo(R.mipmap.ic_channel);

        config.getLogo().setMinimumHeight(1000);
        config.getLogo().setMinimumHeight(1000);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);

    }
}