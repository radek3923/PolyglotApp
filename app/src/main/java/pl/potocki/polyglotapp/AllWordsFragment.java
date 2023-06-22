package pl.potocki.polyglotapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pl.potocki.polyglotapp.api.deepL.DeepLApi;
import pl.potocki.polyglotapp.api.deepL.DeepLApiService;
import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.databinding.FragmentAllWordsBinding;
import pl.potocki.polyglotapp.databinding.FragmentChooseLanguageBinding;
import pl.potocki.polyglotapp.model.language.Language;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllWordsFragment extends Fragment {

    private FragmentAllWordsBinding binding;
    private ItemViewModel viewModel;

    private Observer<List<Word>> wordsObserver = words -> {
        //list of learnt words
        List<String> learntWords = words.stream()
                .filter(Word::isLearned)
                .map(Word::getWordContent)
                .collect(Collectors.toList());

        List<String> notLearntWords = words.stream()
                .filter(Predicate.not(Word::isLearned))
                .map(Word::getWordContent)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapterLearntWords = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, learntWords);
        ArrayAdapter<String> adapterNotLearntWords = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, notLearntWords);
        binding.learntWordsList.setAdapter(adapterLearntWords);
        binding.notLearntWordsList.setAdapter(adapterNotLearntWords);
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAllWordsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        viewModel.getAllWords().observe(getViewLifecycleOwner(), wordsObserver);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllWordsInBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}