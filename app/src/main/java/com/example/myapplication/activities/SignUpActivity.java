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

public class SignUpActivity extends AppCompatActivity {

    EditText signUpUsernameEditText, signUpPasswordEditText;
    Button signUpButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        signUpUsernameEditText = findViewById(R.id.signUpUsername);
        signUpPasswordEditText = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if the user has already signed up
        boolean isUserSignedUp = sharedPreferences.getBoolean("isSignedUp", false);
        if (isUserSignedUp) {
            // User has already signed up, redirect to LoginActivity
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish(); // Finish SignUpActivity
            return; // Exit onCreate method
        }

        // Set click listener for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username and password entered by the user
                String username = signUpUsernameEditText.getText().toString();
                String password = signUpPasswordEditText.getText().toString();

                // Perform sign up action
                signUp(username, password);

                // After successful sign-up, set a shared preference value
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isSignedUp", true);
                editor.apply();
            }
        });
    }

    // Method to perform sign up action
    private void signUp(String username, String password) {
        // Store username and password in SharedPreferences or in a database
        // For example:
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();

        // Redirect to MainActivity after successful signup
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish(); // Finish SignUpActivity
    }

}


