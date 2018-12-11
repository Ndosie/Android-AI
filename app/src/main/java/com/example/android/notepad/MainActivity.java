package com.example.android.notepad;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import static com.example.android.notepad.CreateNoteActivity.EXTRA_NOTE;
import static com.example.android.notepad.CreateNoteActivity.EXTRA_TIMESTAMP;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NOTE = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("Username", "");
            if (!userName.isEmpty()) {
                Toast.makeText(this, "Welcome, You have successfull signIn as " + userName, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Welcome, You have successfull with on username", Toast.LENGTH_LONG).show();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntry>>() {
            @Override
            public void onChanged(@Nullable final List<NoteEntry> notes) {
                // Update the cached copy of the words in the adapter.
                adapter.setNotes(notes);
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        NoteEntry myNote = adapter.getNoteAtPosition(position);
                        Toast.makeText(MainActivity.this, "Deleting " +
                                myNote.getContent(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mNoteViewModel.deleteNote(myNote);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                NoteEntry note = adapter.getNoteAtPosition(position);
                launchCreateNoteActivity(note);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            NoteEntry note = new NoteEntry(data.getStringExtra(EXTRA_NOTE),data.getLongExtra(EXTRA_TIMESTAMP, 0));
            mNoteViewModel.insert(note);
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String note_data = data.getStringExtra(CreateNoteActivity.EXTRA_NOTE);
            long note_timestamp = data.getLongExtra(EXTRA_TIMESTAMP, 0);
            int id = data.getIntExtra(CreateNoteActivity.EXTRA_REPLY_ID, -1);

            if (id != -1) {
                mNoteViewModel.update(new NoteEntry(id, note_data, note_timestamp));
            } else {
                Toast.makeText(this, R.string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void launchCreateNoteActivity( NoteEntry note) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_NOTE, note.getContent());
        intent.putExtra(EXTRA_DATA_ID, note.getId());
        startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
    }
}
