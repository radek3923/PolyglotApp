package pl.potocki.polyglotapp.flashcard;

public class Flashcard {
    private String wordName;
    private String translationWordName;

    public Flashcard(String wordName, String translationWordName) {
        this.wordName = wordName;
        this.translationWordName = translationWordName;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getTranslationWordName() {
        return translationWordName;
    }

    public void setTranslationWordName(String translationWordName) {
        this.translationWordName = translationWordName;
    }
}
