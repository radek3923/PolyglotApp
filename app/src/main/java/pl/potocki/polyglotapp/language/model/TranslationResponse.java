package pl.potocki.polyglotapp.language.model;

import java.util.List;

public class TranslationResponse {
    private List<Translation> translations;

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
