package com.example.android.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.android.notepad.MainActivity.EXTRA_DATA_ID;
import static com.example.android.notepad.MainActivity.EXTRA_DATA_UPDATE_NOTE;

public class CreateNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "com.example.android.notepad.NOTE";
    public static final String EXTRA_TIMESTAMP = "com.example.android.notepad.TIMESTAMP";
    public static final String EXTRA_REPLY_ID = "com.example.android.notepad.REPLY_ID";

    private EditText mEditNote;

    private NoteViewModel mNoteViewModel;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditNote = findViewById(R.id.note_edit_text);
        mId = -1 ;

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String note = extras.getString(EXTRA_DATA_UPDATE_NOTE, "");
            if (!note.isEmpty()) {
                mEditNote.setText(note);
                mEditNote.setSelection(note.length());
                mEditNote.requestFocus();
            }
        }

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditNote.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String content = mEditNote.getText().toString();
                    long timestamp = System.currentTimeMillis();
                    replyIntent.putExtra(EXTRA_NOTE, content);
                    replyIntent.putExtra(EXTRA_TIMESTAMP, timestamp);
                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                        /*mId = extras.getInt(EXTRA_DATA_ID, -1);
                        if (mId != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, mId);
                        }*/
                        String firebaseId = extras.getString(EXTRA_DATA_ID, "");
                        if (firebaseId != null) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, firebaseId);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

}
