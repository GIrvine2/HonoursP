package com.example.honoursproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    TextView textView2;
    Button logout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView2 = findViewById(R.id.textView2);
        logout = findViewById(R.id.btnLogout);

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent signout = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(signout);
        });

    }
}