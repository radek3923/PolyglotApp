package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.databinding.FragmentChooseLanguageBinding;
import pl.potocki.polyglotapp.model.language.Language;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import pl.potocki.polyglotapp.model.translation.TranslationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseLanguageFragment extends Fragment {

    private FragmentChooseLanguageBinding binding;
    private ItemViewModel viewModel;

    private List<Language> availableLanguages;

    private final Observer<String> cityObserver = city -> {
        if (city != null) {
            tryToFindSourceLanguage(city);
        }
    };

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentChooseLanguageBinding.inflate(inflater, container, false);
        availableLanguages = new ArrayList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        viewModel.getCityFromGps().observe(getViewLifecycleOwner(), cityObserver);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAvailableLanguages();

        binding.buttonFirst.setOnClickListener(view1 -> {
            Language sourceLang = (Language) availableLanguages.get(binding.sourceLangSpinner.getSelectedItemPosition());
            Language targetLang = (Language) availableLanguages.get(binding.targetLangSpinner.getSelectedItemPosition());

            if (sourceLang != null && !sourceLang.equals(targetLang)) {
                SelectedLanguages selectedLanguage = new SelectedLanguages(sourceLang, targetLang);
                viewModel.setData(selectedLanguage);

                NavHostFragment.findNavController(ChooseLanguageFragment.this)
                        .navigate(R.id.action_ChooseLanguageFragment_to_ChooseGameFragment);
            } else {
                Toast.makeText(getActivity(), "Selected languages must be different", Toast.LENGTH_SHORT).show();
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
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<Language[]> call, @NonNull Response<Language[]> response) {
                availableLanguages = Arrays.asList(response.body());

                String[] languages = availableLanguages.stream()
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

    public void tryToFindSourceLanguage(String city) {
        List<String> list = new ArrayList<>();
        list.add(city);

        DeepLApiService deepLApiService = DeepLApi.getRetrofitInstance().create(DeepLApiService.class);
        Call<TranslationResponse> callToTranslateInTargetLanguage = deepLApiService.getTranslatedText(list, "EN");
        callToTranslateInTargetLanguage.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<TranslationResponse> call, @NonNull Response<TranslationResponse> response) {
                TranslationResponse translationResponse = response.body();
                String detectedLanguageId = translationResponse.getTranslations().get(0).getDetected_source_language();

                Language detectedLanguage = null;
                for (Language language : availableLanguages) {
                    if (language.getLanguage().equals(detectedLanguageId)) {
                        detectedLanguage = language;
                        break;
                    }
                }

                if (detectedLanguage != null) {
                    int selectedIndex = availableLanguages.indexOf(detectedLanguage);
                    binding.sourceLangSpinner.setSelection(selectedIndex);
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {
            }
        });
    }
}