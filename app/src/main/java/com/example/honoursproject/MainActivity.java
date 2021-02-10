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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginBtn;
    TextView tvnoAccount;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.emailId);
        loginPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        tvnoAccount = findViewById(R.id.tvnoAccount);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if(mFirebaseUser != null) {
                    //Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

                }

            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String pw = loginPassword.getText().toString();
                if(email.isEmpty()) {
                    loginEmail.setError("Please enter a email");
                    loginEmail.requestFocus();
                }
                else if(pw.isEmpty()) {
                    loginPassword.setError("Please enter a password");
                    loginPassword.requestFocus();
                }
                else if(email.isEmpty() && pw.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();

                }

                else if(!(email.isEmpty() && pw.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Could not login, Try again!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent home = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(home);
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvnoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

}