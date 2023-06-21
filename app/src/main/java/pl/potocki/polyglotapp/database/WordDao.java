package pl.potocki.polyglotapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM words")
    List<Word> getAllWords();

    @Query("SELECT * FROM words WHERE id = :id")
    Word getWordById(int id);

    @Insert
    void insertWord(Word word);
}
