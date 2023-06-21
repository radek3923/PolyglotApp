package pl.potocki.polyglotapp.model.flashcards;

public class Flashcard {
    private String wordSourceLanguage;
    private String wordTargetLanguage;

    public Flashcard(String wordName, String translationWordName) {
        this.wordSourceLanguage = wordName;
        this.wordTargetLanguage = translationWordName;
    }

    public String getWordSourceLanguage() {
        return wordSourceLanguage;
    }

    public void setWordSourceLanguage(String wordSourceLanguage) {
        this.wordSourceLanguage = wordSourceLanguage;
    }

    public String getWordTargetLanguage() {
        return wordTargetLanguage;
    }

    public void setWordTargetLanguage(String wordTargetLanguage) {
        this.wordTargetLanguage = wordTargetLanguage;
    }
}
