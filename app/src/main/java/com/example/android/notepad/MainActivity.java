package com.example.android.notepad;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static com.example.android.notepad.CreateNoteActivity.EXTRA_NOTE;
import static com.example.android.notepad.CreateNoteActivity.EXTRA_TIMESTAMP;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;

    private NoteViewModel mNoteViewModel;
    private String mData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntry>>() {
            @Override
            public void onChanged(@Nullable final List<NoteEntry> notes) {
                for (int i=0; i<notes.size(); i++) {
                    mData += notes.get(i).getContent() + "\n";
                }
            }
        });

        final Button button = findViewById(R.id.button_fetch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar mySnackbar = Snackbar.make(view, mData, Snackbar.LENGTH_LONG);
                mySnackbar.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            NoteEntry note = new NoteEntry(data.getStringExtra(EXTRA_NOTE),data.getLongExtra(EXTRA_TIMESTAMP, 0));
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
