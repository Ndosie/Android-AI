package com.example.android.notepad;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {NoteEntry.class, User.class}, version = 2, exportSchema = false)
public abstract class NotePadDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
    public abstract UserDao userDao();
    private static NotePadDatabase INSTANCE;

    static NotePadDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NotePadDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotePadDatabase.class, "notes_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
