package pl.potocki.polyglotapp.api.randomWord;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomWordApi {
    private static Retrofit retrofit;
    private static String BASE_URL = "https://random-word-api.vercel.app/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
