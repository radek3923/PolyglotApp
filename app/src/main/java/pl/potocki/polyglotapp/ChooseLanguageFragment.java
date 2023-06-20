package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;

import pl.potocki.polyglotapp.databinding.FragmentChooseLanguageBinding;
import pl.potocki.polyglotapp.language.api.DeepLApi;
import pl.potocki.polyglotapp.language.api.DeepLApiService;
import pl.potocki.polyglotapp.language.model.Language;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseLanguageFragment extends Fragment {

    private FragmentChooseLanguageBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentChooseLanguageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAvailableLanguages();

//        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
//        Call<String[]> call = randomWordApiService.getRandomWords();
//        call.enqueue(new Callback<String[]>() {
//            @Override
//            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
//                System.out.println();
//                String[] words = response.body();
//                for (String word : words){
//                    System.out.println(word);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String[]> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sourceLang = (String) binding.sourceLangSpinner.getSelectedItem();
                String targetLang = (String) binding.targetLangSpinner.getSelectedItem();

                if (sourceLang != null && !sourceLang.equals(targetLang)) {
                    NavHostFragment.findNavController(ChooseLanguageFragment.this)
                            .navigate(R.id.action_ChooseLanguageFragment_to_ChooseGameFragment);
                } else {
                    Toast.makeText(getActivity(), "Selected languages must be different", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setAvailableLanguages() {
        DeepLApiService deepLApiService = DeepLApi.getRetrofitInstance().create(DeepLApiService.class);
        Call<Language[]> call = deepLApiService.getLanguageData();
        call.enqueue(new Callback<Language[]>() {

            @Override
            public void onResponse(@NonNull Call<Language[]> call, @NonNull Response<Language[]> response) {
                String[] languages = Arrays.stream(response.body())
                        .map(Language::getName)
                        .toArray(String[]::new);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languages);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.sourceLangSpinner.setAdapter(adapter);
                binding.targetLangSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Language[]> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}