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

import java.util.Arrays;

import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;
import pl.potocki.polyglotapp.language.model.Language;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApi;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardsFragment extends Fragment {

    private FragmentFlashcardsBinding binding;
    private boolean isFlipped = false;
    private Animator flipLeftHalfAnimator;
    private Animator flipLeftFullAnimator;
    private Animator flipRightHalfAnimator;
    private Animator flipRightFullAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRandomWords();
        setFlashcardsAnimators();


        binding.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlipped) {
                    flipRight();
                    isFlipped = true;
                } else {
                    flipLeft();
                    isFlipped = false;
                }
            }
        });

        binding.yesButtonFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Klikam Tak");
            }
        });

        binding.noButtonFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Klikam Nie");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void flipRight() {
        flipRightHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.wordTextView.setText("Prawo");
            }
        });
        flipRightHalfAnimator.start();
        flipRightFullAnimator.start();
    }

    private void flipLeft() {
        flipLeftHalfAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.wordTextView.setText("Lewo");
            }
        });
        flipLeftHalfAnimator.start();
        flipLeftFullAnimator.start();
    }

    public void setRandomWords() {
        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
        Call<String[]> call = randomWordApiService.getRandomWords();
        call.enqueue(new Callback<String[]>() {

            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                System.out.println("Wylosowałem słowa: ");
                for (String word: response.body()) {
                    System.out.println(word);
                }

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