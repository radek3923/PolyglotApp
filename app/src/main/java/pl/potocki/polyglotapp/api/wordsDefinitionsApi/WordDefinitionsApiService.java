package pl.potocki.polyglotapp.api.wordsDefinitionsApi;

import pl.potocki.polyglotapp.model.word.WordDefinitions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WordDefinitionsApiService {
    @Headers({
            "X-RapidAPI-Host: wordsapiv1.p.rapidapi.com",
            "X-RapidAPI-Key: 310fb7c656msh8075eae48abbbffp183ad0jsn7d389d965ab3"
    })
    @GET("words/{word}/definitions")
    Call<WordDefinitions> getWordDefinitions(@Path("word") String word);
}
