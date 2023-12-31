package pl.potocki.polyglotapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pl.potocki.polyglotapp.communicate.ItemViewModel;
import pl.potocki.polyglotapp.communicate.WordAdapter;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.databinding.FragmentAllWordsBinding;

public class AllWordsFragment extends Fragment {

    private FragmentAllWordsBinding binding;
    private ItemViewModel viewModel;

    private ArrayAdapter<Word> adapterLearntWords;
    private ArrayAdapter<Word> adapterNotLearntWords;
    private List<Word> learntWords;
    private List<Word> notLearntWords;

    private Observer<List<Word>> wordsObserver = words -> {
        learntWords = words.stream()
                .filter(Word::isLearned)
                .collect(Collectors.toList());

        notLearntWords = words.stream()
                .filter(Predicate.not(Word::isLearned))
                .collect(Collectors.toList());

        adapterLearntWords = new WordAdapter(requireContext(), learntWords);
        adapterNotLearntWords = new WordAdapter(requireContext(), notLearntWords);
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

        binding.learntWordsList.setOnItemClickListener((adapterView, view1, position, id) -> {
            changeItemColor(position, true);
        });

        binding.notLearntWordsList.setOnItemClickListener((adapterView, view1, position, id) -> {
            changeItemColor(position, false);
        });

        binding.modifyButton.setOnClickListener(view1 -> {
            int learntWordsSelectedPosition = binding.learntWordsList.getCheckedItemPosition();
            int notLearntWordsSelectedPosition = binding.notLearntWordsList.getCheckedItemPosition();

            if (learntWordsSelectedPosition != AdapterView.INVALID_POSITION) {
                Word word = learntWords.get(learntWordsSelectedPosition);
                showEditDescriptionDialog(word);

            } else if (notLearntWordsSelectedPosition != AdapterView.INVALID_POSITION) {
                Word word = notLearntWords.get(notLearntWordsSelectedPosition);
                showEditDescriptionDialog(word);
            } else {
                Toast.makeText(requireContext(), "No word selected", Toast.LENGTH_SHORT).show();
            }
        });

        binding.transferButton.setOnClickListener(v -> {
            int learntWordsSelectedPosition = binding.learntWordsList.getCheckedItemPosition();
            int notLearntWordsSelectedPosition = binding.notLearntWordsList.getCheckedItemPosition();
            setAllItemsOnWhiteColor();


            if (learntWordsSelectedPosition != AdapterView.INVALID_POSITION) {
                Word word = learntWords.get(learntWordsSelectedPosition);
                notLearntWords.add(word);
                learntWords.remove(learntWordsSelectedPosition);
                adapterLearntWords.notifyDataSetChanged();
                adapterNotLearntWords.notifyDataSetChanged();
                binding.learntWordsList.clearChoices();

                //update this word to database
                word.setLearned(false);
                viewModel.updateWordInBackground(word);

            } else if (notLearntWordsSelectedPosition != AdapterView.INVALID_POSITION) {
                Word word = notLearntWords.get(notLearntWordsSelectedPosition);
                learntWords.add(word);
                notLearntWords.remove(notLearntWordsSelectedPosition);
                adapterNotLearntWords.notifyDataSetChanged();
                adapterLearntWords.notifyDataSetChanged();
                binding.notLearntWordsList.clearChoices();

                //update this word to database
                word.setLearned(true);
                viewModel.updateWordInBackground(word);
            } else {
                Toast.makeText(requireContext(), "No word selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    private void showEditDescriptionDialog(Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Description");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView descriptionLabel = new TextView(requireContext());
        layout.addView(descriptionLabel);

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        String text = word.getWordContent();

        input.setText(text);
        input.setSelection(0, text.length());
        layout.addView(input);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {

            if (!input.getText().toString().equals(text)) {

                String newDescription = input.getText().toString();
                word.setWordContent(newDescription);

                viewModel.updateWordInBackground(word);

                Toast.makeText(requireContext(), "Word  updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No description changes", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.setNeutralButton("Delete", (dialog, which) -> {
            viewModel.deleteWordInBackground(word);
            if (word.isLearned()) {
                learntWords.remove(word);
                adapterLearntWords.notifyDataSetChanged();
            } else {
                notLearntWords.remove(word);
                adapterNotLearntWords.notifyDataSetChanged();
            }
            Toast.makeText(requireContext(), "Word deleted", Toast.LENGTH_SHORT).show();
            dialog.cancel();
        });

        builder.show();
    }

    private void changeItemColor(int position, boolean isLearntList) {
        ListView listViewToGrey = isLearntList ? binding.learntWordsList : binding.notLearntWordsList;
        setAllItemsOnWhiteColor();
        View selectedView = listViewToGrey.getChildAt(position);
        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.GRAY);
        }
    }

    private void setAllItemsOnWhiteColor(){
        ListView listViewLearnedWords = binding.learntWordsList;
        ListView listViewNotLearnedWords = binding.notLearntWordsList;
        for (int i = 0; i < listViewLearnedWords.getCount(); i++) {
            View itemView = listViewLearnedWords.getChildAt(i);
            if (itemView != null) {
                itemView.setBackgroundColor(Color.WHITE);
            }
        }
        for (int i = 0; i < listViewNotLearnedWords.getCount(); i++) {
            View itemView = listViewNotLearnedWords.getChildAt(i);
            if (itemView != null) {
                itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

}