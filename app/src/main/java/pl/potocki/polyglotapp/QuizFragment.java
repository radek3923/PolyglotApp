package pl.potocki.polyglotapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.potocki.polyglotapp.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_GRAY = 0xFF808080;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}