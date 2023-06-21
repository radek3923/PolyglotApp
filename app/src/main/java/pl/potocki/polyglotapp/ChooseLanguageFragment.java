package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.databinding.FragmentChooseLanguageBinding;
import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.model.language.Language;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseLanguageFragment extends Fragment {

    private FragmentChooseLanguageBinding binding;
    private ItemViewModel viewModel;

    private List<Language> availableLanguages;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentChooseLanguageBinding.inflate(inflater, container, false);
        availableLanguages = new ArrayList<>();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAvailableLanguages();

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Language sourceLang = (Language) availableLanguages.get(binding.sourceLangSpinner.getSelectedItemPosition());
                Language targetLang = (Language) availableLanguages.get(binding.targetLangSpinner.getSelectedItemPosition());

                if (sourceLang != null && !sourceLang.equals(targetLang)) {

                    viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

                    System.out.println("Source: " + sourceLang.getName());
                    System.out.println("Target: " + targetLang.getName());

                    SelectedLanguages selectedLanguage = new SelectedLanguages(sourceLang, targetLang);
                    viewModel.setData(selectedLanguage);

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
}