package com.example.android.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "com.example.android.notepad.NOTE";
    public static final String EXTRA_TIMESTAMP = "com.example.android.notepad.TIMESTAMP";

    private EditText mEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditNote = findViewById(R.id.note_edit_text);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditNote.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditNote.getText().toString();
                    long timestamp = System.currentTimeMillis();
                    replyIntent.putExtra(EXTRA_NOTE, word);
                    replyIntent.putExtra(EXTRA_TIMESTAMP, timestamp);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

}
