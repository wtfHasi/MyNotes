package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    EditText loginPasswordEditText;
    Button loginButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if the user has signed up
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isSignedUp = sharedPreferences.getBoolean("isSignedUp", false);

        if (!isSignedUp) {
            // User has not signed up, redirect them to the sign-up screen
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        }

        // Initialize views
        loginPasswordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve password entered by the user
                String password = loginPasswordEditText.getText().toString();

                // Verify password
                if (verifyPassword(password)) {
                    // Password is correct, redirect to MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // Finish LoginActivity to prevent going back when pressing back button
                } else {
                    // Password is incorrect, show error message
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to verify password
    private boolean verifyPassword(String password) {
        // Retrieve saved password from SharedPreferences
        String savedPassword = sharedPreferences.getString("password", "");

        // Check if the entered password matches the saved password
        return password.equals(savedPassword);
    }
}



