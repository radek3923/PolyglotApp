package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import pl.potocki.polyglotapp.databinding.FragmentChooseGameBinding;

public class ChooseGameFragment extends Fragment {

    private FragmentChooseGameBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentChooseGameBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.flashcardsTextView.setOnClickListener(view1 -> {
            System.out.println("Przenoszę do fiszek");
            NavHostFragment.findNavController(ChooseGameFragment.this)
                    .navigate(R.id.action_ChooseGameFragment_to_FlashcardsFragment);

        });

        binding.quizTextView.setOnClickListener(view1 -> {
            System.out.println("Przenoszę do quizu");
            NavHostFragment.findNavController(ChooseGameFragment.this)
                    .navigate(R.id.action_ChooseGameFragment_to_QuizFragment);
        });

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChooseGameFragment.this)
                        .navigate(R.id.action_ChooseGameFragment_to_ChooseLanguageFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}