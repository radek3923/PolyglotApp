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

import pl.potocki.polyglotapp.databinding.FragmentFlashcardsBinding;

public class FlashcardsFragment extends Fragment {

    private FragmentFlashcardsBinding binding;
    private boolean isFlipped = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFlashcardsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
                if (!isFlipped) {
                    isFlipped = true;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void flipCard() {
        Animator flipAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_animation);
        flipAnimator.setTarget(binding.cardContainer);
        flipAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        flipAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Wykonaj akcję po zakończeniu animacji, np. pokaż tłumaczenie
            }
        });
        flipAnimator.start();
    }
}