package pl.potocki.polyglotapp.api.wordsDefinitionsApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordDefinitionsApi {
    private static Retrofit retrofit;
    private static String BASE_URL = "https://wordsapiv1.p.rapidapi.com/";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
