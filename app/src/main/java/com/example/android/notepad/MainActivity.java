package com.example.android.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.notepad.CreateNoteActivity.EXTRA_NOTE;
import static com.example.android.notepad.CreateNoteActivity.EXTRA_REPLY_ID;
import static com.example.android.notepad.CreateNoteActivity.EXTRA_TIMESTAMP;
import static com.example.android.notepad.GoogleSignInActivity.EXTRA_EMAIL;
import static com.example.android.notepad.GoogleSignInActivity.EXTRA_NAME;
import static com.example.android.notepad.GoogleSignInActivity.EXTRA_NEW_SIGNIN;
import static com.example.android.notepad.GoogleSignInActivity.EXTRA_URL;

public class MainActivity extends AppCompatActivity {
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NOTE = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private String mUserId;

    private GoogleSignInClient mGoogleSignInClient;

    private NoteViewModel mNoteViewModel;

    private ArrayList mNotes = new ArrayList<NoteEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }*/

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth, Database reference and user id
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserId = mFirebaseUser.getUid();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //final NoteListAdapter adapter = new NoteListAdapter(this);
        final NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntry>>() {
            @Override
            public void onChanged(@Nullable final List<NoteEntry> notes) {
                // Update the cached copy of the notes in the adapter.
                adapter.setNotes(notes);
            }
        });*/

        Query notesQuery = mDatabaseReference.child("users").child(mUserId).child("notes");
        notesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot noteSnapshot, @Nullable String s) {
                mNotes.add(new NoteEntry(noteSnapshot.getKey(),(String) noteSnapshot.child("content").getValue(),
                        (Long) noteSnapshot.child("timestamp").getValue()));
                adapter.setNotes(mNotes);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email  = extras.getString(EXTRA_EMAIL, "");
            String name = extras.getString(EXTRA_NAME, "");
            String url = extras.getString(EXTRA_URL, "");
            boolean newSignIn = extras.getBoolean(EXTRA_NEW_SIGNIN, true);

            if(newSignIn) {
                User user = new User(email, name, url);
                mDatabaseReference.child("users")
                        .push()
                        .setValue(user);
                //mNoteViewModel.insertUser(user);
            }

            if (!name.isEmpty()) {
                Toast.makeText(this, "Welcome, You have successfully signIn as " + name, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Welcome, You have successfully with on username", Toast.LENGTH_LONG).show();
            }
        }

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

                        // Delete the note
                        //mNoteViewModel.deleteNote(myNote);
                        deleteNote(myNote.getFirebaseId());
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
            mDatabaseReference.child("users")
                    .child(mUserId)
                    .child("notes")
                    .push()
                    .setValue(note);
            //mNoteViewModel.insertNote(note);
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String note_content = data.getStringExtra(CreateNoteActivity.EXTRA_NOTE);
            long note_timestamp = data.getLongExtra(EXTRA_TIMESTAMP, 0);
            //int id = data.getIntExtra(CreateNoteActivity.EXTRA_REPLY_ID, -1);
            String firebaseId = data.getStringExtra(EXTRA_REPLY_ID);

            if (firebaseId != null) {
                //mNoteViewModel.update(new NoteEntry(id, note_data, note_timestamp));

                DatabaseReference entryRef = mDatabaseReference.child("users")
                        .child(mUserId)
                        .child("notes")
                        .child(firebaseId);
                Map<String,Object> entryMap = new HashMap<String,Object>();
                entryMap.put("content", note_content);
                entryMap.put("timestamp", note_timestamp);
                entryRef.updateChildren(entryMap);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
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
        //intent.putExtra(EXTRA_DATA_ID, note.getId());
        intent.putExtra(EXTRA_DATA_ID, note.getFirebaseId());
        startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
    }


    private void deleteNote(String noteId){
        mDatabaseReference.child("users")
                .child(mUserId)
                .child("notes")
                .child(noteId)
                .removeValue();
        Toast.makeText(this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_signout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //mNoteViewModel.deleteUser();
                        //mNoteViewModel.deleteAllNotes();
                        finish();
                    }
                });
    }
}
