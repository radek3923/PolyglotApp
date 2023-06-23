package pl.potocki.polyglotapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApi;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApiService;
import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;
import pl.potocki.polyglotapp.model.flashcards.Flashcard;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import pl.potocki.polyglotapp.model.translation.TranslationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardsFragment extends Fragment {

    private FragmentFlashcardsBinding binding;
    private ItemViewModel viewModel;
    private SelectedLanguages selectedLanguages;

    private boolean isOnWordSide = true;
    private Animator flipLeftHalfAnimator;
    private Animator flipLeftFullAnimator;
    private Animator flipRightHalfAnimator;
    private Animator flipRightFullAnimator;

    private List<Flashcard> flashcards;
    private int currentFlashcardIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);
        flashcards = new ArrayList<>();
        generateRandomWords();

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        selectedLanguages = viewModel.getSelectedItem().getValue();

        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFlashcardsAnimators();

        System.out.println(selectedLanguages.getSourceLanguage().getName());

        binding.cardContainer.setOnClickListener(v -> {
            if (isOnWordSide) {
                flipOnTranslateSide();
                isOnWordSide = false;
                System.out.println("Turning flashcard on translated side");
            } else {
                flipOnWordSide();
                isOnWordSide = true;
                System.out.println("Turning flashcard on not translated side");
            }
        });

        binding.seeMyWordsButtonFlashcards.setOnClickListener(v -> {
            NavHostFragment.findNavController(FlashcardsFragment.this)
                    .navigate(R.id.action_FlashcardsFragment_to_allWordsFragment);
        });

        binding.yesButtonFlashcards.setOnClickListener(v -> {
            Word word = new Word(flashcards.get(currentFlashcardIndex).getWordTargetLanguage(),
                    selectedLanguages.getTargetLanguage().getLanguage(),
                    true);
            viewModel.addWordInBackground(word);
            showNextWord();
        });

        binding.noButtonFlashcards.setOnClickListener(v -> {
            Word word = new Word(flashcards.get(currentFlashcardIndex).getWordTargetLanguage(),
                    selectedLanguages.getTargetLanguage().getLanguage(),
                    false);
            viewModel.addWordInBackground(word);
            showNextWord();
        });
    }

    private void showNextWord() {
        if (flashcards.size() > currentFlashcardIndex + 1) {
            currentFlashcardIndex++;
            if (isOnWordSide) {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordSourceLanguage());
            } else {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordTargetLanguage());
            }
        } else {
            currentFlashcardIndex = 0;
            System.out.println("Starting generating new words");
            generateRandomWords();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void flipOnWordSide() {
        flipRightHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordSourceLanguage());
            }
        });
        flipRightHalfAnimator.start();
        flipRightFullAnimator.start();
    }

    private void flipOnTranslateSide() {
        flipLeftHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordTargetLanguage());
            }
        });
        flipLeftHalfAnimator.start();
        flipLeftFullAnimator.start();
    }

    public void setFlashcardText(String text) {
        if (binding.wordTextView != null) {
            binding.wordTextView.setText(text);
        }
    }

    public void generateRandomWords() {
        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
        Call<String[]> call = randomWordApiService.getRandomWords(10);
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                assert response.body() != null;
                flashcards.clear();
                for (String word : response.body()) {
                    Flashcard flashcard = new Flashcard(word, "translation");
                    flashcards.add(flashcard);
                }
                translateGeneratedWords(flashcards);
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setFlashcardsAnimators() {
        flipRightHalfAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_right_half_animation);
        flipRightHalfAnimator.setTarget(binding.cardContainer);
        flipRightHalfAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        flipRightFullAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_right_full_animation);
        flipRightFullAnimator.setTarget(binding.cardContainer);
        flipRightFullAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        flipLeftHalfAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_left_half_animation);
        flipLeftHalfAnimator.setTarget(binding.cardContainer);
        flipLeftHalfAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        flipLeftFullAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_left_full_animation);
        flipLeftFullAnimator.setTarget(binding.cardContainer);
        flipLeftFullAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void translateGeneratedWords(List<Flashcard> flashcards) {
        DeepLApiService deepLApiService = DeepLApi.getRetrofitInstance().create(DeepLApiService.class);

        List<String> wordsToTranslate = flashcards.stream()
                .map(Flashcard::getWordSourceLanguage)
                .collect(Collectors.toList());

        Call<TranslationResponse> callToTranslateInTargetLanguage = deepLApiService.getTranslatedText(wordsToTranslate, selectedLanguages.getTargetLanguage().getLanguage());
        callToTranslateInTargetLanguage.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                TranslationResponse translationResponse = (TranslationResponse) response.body();
                IntStream.range(0, flashcards.size())
                        .forEach(i -> flashcards.get(i).setWordTargetLanguage(translationResponse.getTranslations().get(i).getText()));
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });


        Call<TranslationResponse> callToTranslateInSourceLanguage = deepLApiService.getTranslatedText(wordsToTranslate, selectedLanguages.getSourceLanguage().getLanguage());
        callToTranslateInSourceLanguage.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<TranslationResponse> call, @NonNull Response<TranslationResponse> response) {
                TranslationResponse translationResponse = (TranslationResponse) response.body();
                IntStream.range(0, flashcards.size())
                        .forEach(i -> flashcards.get(i).setWordSourceLanguage(translationResponse.getTranslations().get(i).getText()));
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordSourceLanguage());
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });
    }
}