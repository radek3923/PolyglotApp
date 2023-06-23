package pl.potocki.polyglotapp.communicate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.potocki.polyglotapp.database.Word;

public class WordAdapter extends ArrayAdapter<Word> {
    private final List<Word> words;

    public WordAdapter(Context context, List<Word> words) {
        super(context, 0, words);
        this.words = words;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textViewName = convertView.findViewById(android.R.id.text1);
        Word word = words.get(position);
        textViewName.setText(word.getWordContent());

        return convertView;
    }
}