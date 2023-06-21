package pl.potocki.polyglotapp.api.randomWord;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RandomWordApiService {
    @GET("api?words=5")
    Call<String[]> getRandomWords();
}

