package pl.potocki.polyglotapp.language.api;

import pl.potocki.polyglotapp.language.model.Language;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface DeepLApiService {

    @Headers("Authorization: DeepL-Auth-Key 9da9efe1-47a9-3c46-a204-8a99cc511512:fx")
    @GET("languages")
    Call<Language[]> getLanguageData();
}
