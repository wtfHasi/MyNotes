package com.example.myapplication.listeners;

import com.example.myapplication.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
