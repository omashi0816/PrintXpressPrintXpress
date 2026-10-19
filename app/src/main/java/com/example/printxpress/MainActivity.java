package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // App entry → go to Splash screen
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);

        // Close MainActivity so user can't come back
        finish();
    }
}