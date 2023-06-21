package pl.potocki.polyglotapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.database.AppDatabase;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.database.WordDao;
import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;
import pl.potocki.polyglotapp.model.flashcards.Flashcard;
import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import pl.potocki.polyglotapp.model.translation.TranslationResponse;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApi;
import pl.potocki.polyglotapp.api.randomWord.RandomWordApiService;
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

        binding.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnWordSide) {
                    flipOnTranslateSide();
                    isOnWordSide = false;
                    System.out.println("Strona fiszki z tłumaczeniem");
                } else {
                    flipOnWordSide();
                    isOnWordSide = true;
                    System.out.println("Strona fiszki bez tłumaczenia");
                }
            }
        });

        binding.yesButtonFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextWord();


                Word word = new Word();
                word.setWordContent("Example");
                word.setLearned(false);

                addWordInBackground(word);
                System.out.println("Klikam Tak");
            }
        });

        binding.noButtonFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextWord();
                System.out.println("Klikam Nie");
            }
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
            System.out.println("Skończyły się wygenerowane słowa. Tutaj trzeba pewnie wygenerować nowe");
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
        binding.wordTextView.setText(text);
    }

    public void generateRandomWords() {
        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
        Call<String[]> call = randomWordApiService.getRandomWords();
        call.enqueue(new Callback<String[]>() {

            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                assert response.body() != null;
                flashcards.clear();
                for (String word : response.body()) {
                    Flashcard flashcard = new Flashcard(word, "translation");
                    flashcards.add(flashcard);
                }
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordSourceLanguage());
                translateGeneratedWords(flashcards);
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                System.out.println("Bład przy losowaniu słow");
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
        callToTranslateInTargetLanguage.enqueue(new Callback<TranslationResponse>() {

            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                TranslationResponse translationResponse = (TranslationResponse) response.body();
                IntStream.range(0, flashcards.size())
                        .forEach(i -> flashcards.get(i).setWordTargetLanguage(translationResponse.getTranslations().get(i).getText()));

                for (Flashcard flash : flashcards
                ) {
                    System.out.println("Target: " + flash.getWordTargetLanguage());
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });


        Call<TranslationResponse> callToTranslateInSourceLanguage = deepLApiService.getTranslatedText(wordsToTranslate, selectedLanguages.getSourceLanguage().getLanguage());
        callToTranslateInSourceLanguage.enqueue(new Callback<TranslationResponse>() {

            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                TranslationResponse translationResponse = (TranslationResponse) response.body();
                IntStream.range(0, flashcards.size())
                        .forEach(i -> flashcards.get(i).setWordSourceLanguage(translationResponse.getTranslations().get(i).getText()));

                for (Flashcard flash : flashcards
                ) {
                    System.out.println("Source: " + flash.getWordSourceLanguage());
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });

    }

    public void addWordInBackground(Word word) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                AppDatabase appDatabase = AppDatabase.getInstance(getContext());
                WordDao wordDao = appDatabase.wordDao();
                wordDao.insertWord(word);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Added word to Database");
                    }
                });
            }
        });
    }

}