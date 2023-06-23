package pl.potocki.polyglotapp.api.randomWord;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomWordApiService {
    @GET("api")
    Call<String[]> getRandomWords(@Query("words") int numberOfWords);
}

