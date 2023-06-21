package pl.potocki.polyglotapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "word_database")
                    .build();
        }
        return instance;
    }
}
