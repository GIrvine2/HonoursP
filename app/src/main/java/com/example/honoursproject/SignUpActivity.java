package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * SignUpActivity
 * Users can sign up for an account here and store the information into firebase Authentication
 */
public class SignUpActivity extends AppCompatActivity {

    EditText emailId, password;
    Button signUpBtn;
    TextView tvLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        signUpBtn = findViewById(R.id.loginBtn);
        tvLogin = findViewById(R.id.tvnoAccount);

        signUpBtn.setOnClickListener(v -> {
            String email = emailId.getText().toString();
            String pw = password.getText().toString();
            if(email.isEmpty()) {
                emailId.setError("Please enter a email");
                emailId.requestFocus();
            }
            else if(pw.isEmpty()) {
                password.setError("Please enter a password");
                password.requestFocus();
            }
            else if(email.isEmpty() && pw.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();

            }

            else if(!(email.isEmpty() && pw.isEmpty())) {
                mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(SignUpActivity.this, task -> {
                    if(!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Sign Up Failed, Try again!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                        finish();

                    }
                });

            }
            else {
                Toast.makeText(SignUpActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(v -> {
            Intent login = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(login);

        });
    }
}