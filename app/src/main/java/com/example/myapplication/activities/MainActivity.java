package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.activities.CreateNoteActivity;
import com.example.myapplication.adapters.NotesAdapter;
import com.example.myapplication.database.NotesDatabase;
import com.example.myapplication.entities.Note;
import com.example.myapplication.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {

    // Constants for request codes used in startActivityForResult() method
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private RecyclerView notesRecyclerView; // RecyclerView instance to display notes
    private List<Note> noteList; // List to hold Note objects
    private NotesAdapter notesAdapter; // Adapter for binding data to the RecyclerView

    // Position of the note item that was clicked (-1 indicates no item clicked)
    private int noteClickedPosition = -1;

    private TextView textViewWelcome;
    private SharedPreferences sharedPreferences;
    private static final String PREF_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Find the TextView
        textViewWelcome = findViewById(R.id.textMyNotes);

        // Retrieve the username from SharedPreferences
        String username = sharedPreferences.getString(PREF_USERNAME, "User");

        // Set the welcome message with the retrieved username
        String welcomeMessage = getString(R.string.welcome_hasith, username);
        textViewWelcome.setText(welcomeMessage);

        // Find and set OnClickListener for the "Add Note" ImageView
        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        // Retrieve notes from database
        getNotes(REQUEST_CODE_SHOW_NOTES);

        // Find the settings ImageView
        ImageView settingsImageView = findViewById(R.id.settings);

        // Set OnClickListener for the settings ImageView
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SettingsActivity when settings ImageView is clicked
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);// Start the activity for result, expecting a response back
    }

    private void getNotes(final int requestCode) {
        // Define an AsyncTask to perform database operations asynchronously
        @SuppressLint("StatisticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            // This method runs in the background thread to fetch notes from the database
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao().getAllNotes(); // Retrieve all notes from the database using DAO
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE);
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }
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


