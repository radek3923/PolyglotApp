package pl.potocki.polyglotapp.randomWord.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RandomWordApiService {
    @GET("api?words=5")
    Call<String[]> getRandomWords();
}

