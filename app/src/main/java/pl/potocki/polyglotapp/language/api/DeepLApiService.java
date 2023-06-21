package pl.potocki.polyglotapp.language.api;

import pl.potocki.polyglotapp.language.model.Language;
import pl.potocki.polyglotapp.language.model.Translation;
import pl.potocki.polyglotapp.language.model.TranslationResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeepLApiService {

    @Headers("Authorization: DeepL-Auth-Key 9da9efe1-47a9-3c46-a204-8a99cc511512:fx")
    @GET("languages")
    Call<Language[]> getLanguageData();

    @Headers("Authorization: DeepL-Auth-Key 9da9efe1-47a9-3c46-a204-8a99cc511512:fx")
    @FormUrlEncoded
    @POST("translate")
    Call<TranslationResponse> getTranslatedText(
            @Field("text") String text,
            @Field("source_lang") String sourceLang,
            @Field("target_lang") String targetLang
    );


}
