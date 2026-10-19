package com.example.printxpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView registerText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerText = findViewById(R.id.registerText);

        loginBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (userEmail.isEmpty()) {
                email.setError("Email is required");
                email.requestFocus();
                return;
            }

            if (userPass.isEmpty()) {
                password.setError("Password is required");
                password.requestFocus();
                return;
            }

            // Firebase Authentication භාවිතයෙන් Login කිරීම
            mAuth.signInWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            String errorMsg = "Invalid Email or Password";
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                errorMsg = "User does not exist";
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMsg = "Invalid Password";
                            }
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
