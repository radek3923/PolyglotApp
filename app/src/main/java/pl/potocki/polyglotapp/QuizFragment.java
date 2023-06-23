package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApi;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApiService;
import pl.potocki.polyglotapp.api.wordsDefinitionsApi.WordDefinitionsApi;
import pl.potocki.polyglotapp.api.wordsDefinitionsApi.WordDefinitionsApiService;
import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.databinding.FragmentQuizBinding;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import pl.potocki.polyglotapp.model.translation.Translation;
import pl.potocki.polyglotapp.model.translation.TranslationResponse;
import pl.potocki.polyglotapp.model.word.Definition;
import pl.potocki.polyglotapp.model.word.WordDefinitions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private ItemViewModel viewModel;
    private SelectedLanguages selectedLanguages;
    List<String> words;
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_GRAY = 0xFF808080;
    private final String nounPartOfSpeech = "noun";
    private int correctAnswerIndex = -1;
    private int selectedAnswerIndex = -1;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        selectedLanguages = viewModel.getSelectedItem().getValue();
        generateRandomWords();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View.OnClickListener buttonClickListener = v -> {
            binding.ansA.setBackgroundColor(COLOR_WHITE);
            binding.ansB.setBackgroundColor(COLOR_WHITE);
            binding.ansC.setBackgroundColor(COLOR_WHITE);
            binding.ansD.setBackgroundColor(COLOR_WHITE);

            v.setBackgroundColor(COLOR_GRAY);

            selectedAnswerIndex = -1;
            if (v.getId() == binding.ansA.getId()) {
                selectedAnswerIndex = 0;
            } else if (v.getId() == binding.ansB.getId()) {
                selectedAnswerIndex = 1;
            } else if (v.getId() == binding.ansC.getId()) {
                selectedAnswerIndex = 2;
            } else if (v.getId() == binding.ansD.getId()) {
                selectedAnswerIndex = 3;
            }
        };

        binding.submitBtn.setOnClickListener(view1 -> {
            System.out.println("Submitting answer");
            if (selectedAnswerIndex == -1) {
                Toast.makeText(getActivity(), "No answer selected", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedAnswerIndex == correctAnswerIndex) {
                Toast.makeText(getActivity(), "Correct answer!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Bad answer", Toast.LENGTH_SHORT).show();
            }
            binding.ansA.setBackgroundColor(COLOR_WHITE);
            binding.ansB.setBackgroundColor(COLOR_WHITE);
            binding.ansC.setBackgroundColor(COLOR_WHITE);
            binding.ansD.setBackgroundColor(COLOR_WHITE);

            generateRandomWords();
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


    private void setWordDefinition(List<String> words) {
        WordDefinitionsApiService wordDefinitionsApiService = WordDefinitionsApi.getRetrofitInstance().create(WordDefinitionsApiService.class);

        Random random = new Random();
        correctAnswerIndex = random.nextInt(4);

        String correctWord = words.get(correctAnswerIndex);

        Call<WordDefinitions> call = wordDefinitionsApiService.getWordDefinitions(correctWord);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<WordDefinitions> call, @NonNull Response<WordDefinitions> response) {
                WordDefinitions wordDefinitions = response.body();
                if (wordDefinitions == null) {
                    generateRandomWords();
                    return;
                }
                List<Definition> definitions = wordDefinitions.getDefinitions().stream()
                        .filter(definition -> nounPartOfSpeech.equals(definition.getPartOfSpeech()))
                        .collect(Collectors.toList());

                if (!definitions.isEmpty()) {
                    translateGeneratedWords(words, definitions.get(0).getDefinition());
                } else {
                    if (!wordDefinitions.getDefinitions().isEmpty()) {
                        translateGeneratedWords(words, wordDefinitions.getDefinitions().get(0).getDefinition());
                    }
                }
            }

            @Override
            public void onFailure(Call<WordDefinitions> call, Throwable t) {

            }
        });
    }

    public void generateRandomWords() {
        clearingButtons();
        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
        Call<String[]> call = randomWordApiService.getRandomWords(4);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                assert response.body() != null;
                words = Arrays.asList(response.body());
                setWordDefinition(words);
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void translateGeneratedWords(List<String> wordsToTranslate, String definition) {
        DeepLApiService deepLApiService = DeepLApi.getRetrofitInstance().create(DeepLApiService.class);

        List<String> translatedWords = new ArrayList<>(wordsToTranslate);
        translatedWords.add(definition);

        Call<TranslationResponse> callToTranslateInTargetLanguage = deepLApiService.getTranslatedText(translatedWords, selectedLanguages.getTargetLanguage().getLanguage());
        callToTranslateInTargetLanguage.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<TranslationResponse> call, @NonNull Response<TranslationResponse> response) {
                TranslationResponse translationResponse = (TranslationResponse) response.body();

                words = translationResponse.getTranslations().stream()
                        .map(Translation::getText)
                        .collect(Collectors.toList());

                binding.ansA.setText(words.get(0));
                binding.ansB.setText(words.get(1));
                binding.ansC.setText(words.get(2));
                binding.ansD.setText(words.get(3));
                binding.question.setText(words.get(4));
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });
    }

    public void clearingButtons() {
        selectedAnswerIndex = -1;
        correctAnswerIndex = -1;
        binding.ansA.setText("");
        binding.ansB.setText("");
        binding.ansC.setText("");
        binding.ansD.setText("");
        binding.question.setText("");
    }
}