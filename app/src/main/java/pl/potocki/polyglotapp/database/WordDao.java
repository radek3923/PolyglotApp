package pl.potocki.polyglotapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM words")
    List<Word> getAllWords();

    @Query("SELECT * FROM words WHERE id = :id")
    Word getWordById(int id);

    @Update()
    void updateWord(Word word);

    @Insert
    void insertWord(Word word);
}
