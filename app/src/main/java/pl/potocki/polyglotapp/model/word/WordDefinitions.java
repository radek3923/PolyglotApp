package pl.potocki.polyglotapp.model.word;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordDefinitions {
    @SerializedName("word")
    private String word;

    @SerializedName("definitions")
    private List<Definition> definitions;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }
}
