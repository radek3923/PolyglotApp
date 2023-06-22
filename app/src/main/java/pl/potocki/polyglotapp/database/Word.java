package pl.potocki.polyglotapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "word_content")
    private String wordContent;

    @ColumnInfo(name = "language")
    private String language;

    @ColumnInfo(name = "is_learned")
    private boolean isLearned;

    public Word(String wordContent, String language, boolean isLearned) {
        this.wordContent = wordContent;
        this.language = language;
        this.isLearned = isLearned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWordContent() {
        return wordContent;
    }

    public void setWordContent(String wordContent) {
        this.wordContent = wordContent;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", wordContent='" + wordContent + '\'' +
                ", language='" + language + '\'' +
                ", isLearned=" + isLearned +
                '}';
    }
}