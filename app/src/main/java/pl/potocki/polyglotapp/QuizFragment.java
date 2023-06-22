package pl.potocki.polyglotapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pl.potocki.polyglotapp.api.wordsDefinitionsApi.WordDefinitionsApi;
import pl.potocki.polyglotapp.api.wordsDefinitionsApi.WordDefinitionsApiService;
import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.databinding.FragmentQuizBinding;
import pl.potocki.polyglotapp.model.word.Definition;
import pl.potocki.polyglotapp.model.word.WordDefinitions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private ItemViewModel viewModel;
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_GRAY = 0xFF808080;
    private final String nounPartOfSpeech = "noun";



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getAllWordsInBackground();

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ansA.setBackgroundColor(COLOR_WHITE);
                binding.ansB.setBackgroundColor(COLOR_WHITE);
                binding.ansC.setBackgroundColor(COLOR_WHITE);
                binding.ansD.setBackgroundColor(COLOR_WHITE);

                v.setBackgroundColor(COLOR_GRAY);
            }
        };

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Klikam przycisk sumbit");
                //TODO generate new word?

            }
        });

        binding.ansA.setOnClickListener(buttonClickListener);
        binding.ansB.setOnClickListener(buttonClickListener);
        binding.ansC.setOnClickListener(buttonClickListener);
        binding.ansD.setOnClickListener(buttonClickListener);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setNewWordDefinition(Word word) {
        WordDefinitionsApiService wordDefinitionsApiService = WordDefinitionsApi.getRetrofitInstance().create(WordDefinitionsApiService.class);
        Call<WordDefinitions> call = wordDefinitionsApiService.getWordDefinitions(word.getWordContent());
        call.enqueue(new Callback<WordDefinitions>() {

            @Override
            public void onResponse(@NonNull Call<WordDefinitions> call, @NonNull Response<WordDefinitions> response) {
                WordDefinitions wordDefinitions = response.body();
                System.out.println("Ustawiam Definicje");


                if (wordDefinitions == null) {
                    System.out.println("To jest nullem");
                    return;
                }
                List<Definition> definitions = wordDefinitions.getDefinitions().stream()
                        .filter(definition -> nounPartOfSpeech.equals(definition.getPartOfSpeech()))
                        .collect(Collectors.toList());

                if (!definitions.isEmpty()) {
                    binding.question.setText(definitions.get(0).getDefinition());
                } else {
                    if (!wordDefinitions.getDefinitions().isEmpty()) {
                        binding.question.setText(wordDefinitions.getDefinitions().get(0).getDefinition());
                    }
                }
            }

            @Override
            public void onFailure(Call<WordDefinitions> call, Throwable t) {

            }
        });
    }
}