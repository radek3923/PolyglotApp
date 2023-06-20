package pl.potocki.polyglotapp;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;

public class FlashcardsFragment extends Fragment {

    private FragmentFlashcardsBinding binding;
    private boolean isFlipped = false;

    // Dodatkowe zmienne do obsługi dwóch animacji
    private Animator flipRightAnimator;
    private Animator flipLeftAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicjalizacja animacji obracania w prawo
        flipRightAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_right_animation);
        flipRightAnimator.setTarget(binding.cardContainer);
        flipRightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // Inicjalizacja animacji obracania w lewo
        flipLeftAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_left_animation);
        flipLeftAnimator.setTarget(binding.cardContainer);
        flipLeftAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        binding.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlipped) {
                    // Obracanie w prawo
                    flipRight();
                    isFlipped = true;
                } else {
                    // Obracanie w lewo
                    flipLeft();
                    isFlipped = false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void flipRight() {
        flipRightAnimator.start();
        System.out.println("Prawo");
    }

    private void flipLeft() {
        flipLeftAnimator.start();
    }
}