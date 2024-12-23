package com.example.myapplication.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class Settings extends AppCompatActivity {

    private EditText usernameEditText;
    private Button userNameButton;
    private TextView textViewWelcome;
    private SharedPreferences sharedPreferences;
    private static final String PREF_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Find and set onClickListener for the back button
        ImageView image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Handle back button press
            }
        });

        // Initialize views
        usernameEditText = findViewById(R.id.editText_username);
        userNameButton = findViewById(R.id.button);
        textViewWelcome = findViewById(R.id.text_my_notes);

        // Load the saved username, if any
        String savedUsername = sharedPreferences.getString(PREF_USERNAME, "");
        if (!savedUsername.isEmpty()) {
            textViewWelcome.setText(getString(R.string.welcome_hasith, savedUsername));
        }

        // Set click listener for the save button
        userNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUsername();
            }
        });

        // Any additional setup for your settings activity can be done here
    }

    private void saveUsername() {
        // Get the entered username from the EditText
        String newUsername = usernameEditText.getText().toString().trim();

        // Check if the username is empty
        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the username
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USERNAME, newUsername);
        editor.apply();

        // Update the welcome message
        textViewWelcome.setText(getString(R.string.welcome_hasith, newUsername));

        // Display a toast with the new username
        Toast.makeText(this, "Username changed to: " + newUsername, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the username from SharedPreferences
        String username = sharedPreferences.getString(PREF_USERNAME, "User");
        // Set the welcome message with the retrieved username
        String welcomeMessage = getString(R.string.welcome_hasith, username);
        textViewWelcome.setText(welcomeMessage);
    }
}



