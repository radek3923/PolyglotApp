package pl.potocki.polyglotapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApi;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardsFragment extends Fragment {

    private FragmentFlashcardsBinding binding;
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
        generateRandomWords();
        flashcards = new ArrayList<>();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFlashcardsAnimators();

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
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordName());
            } else {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getTranslationWordName());
            }
        }
        else{
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
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordName());
            }
        });
        flipRightHalfAnimator.start();
        flipRightFullAnimator.start();
    }

    private void flipOnTranslateSide() {
        flipLeftHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setFlashcardText(flashcards.get(currentFlashcardIndex).getTranslationWordName());
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
                setFlashcardText(flashcards.get(currentFlashcardIndex).getWordName());
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
}