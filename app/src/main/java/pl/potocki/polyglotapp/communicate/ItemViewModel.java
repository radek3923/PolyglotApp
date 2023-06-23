package pl.potocki.polyglotapp.communicate;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import pl.potocki.polyglotapp.database.Word;
import pl.potocki.polyglotapp.database.WordDao;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;

@HiltViewModel
public class ItemViewModel extends ViewModel {
    private final WordDao wordDao;
    private final MutableLiveData<SelectedLanguages> selectedItem = new MutableLiveData<>();
    private MutableLiveData<List<Word>> allWords = new MutableLiveData<>();

    private MutableLiveData<String> cityFromGps = new MutableLiveData<>();

    @Inject
    public ItemViewModel(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    public void setCityFromGps(String s) {
        cityFromGps.setValue(s);
    }

    public LiveData<String> getCityFromGps(){
        return cityFromGps;
    }

    public void setData(SelectedLanguages selectedLanguages) {
        selectedItem.setValue(selectedLanguages);
    }

    public LiveData<SelectedLanguages> getSelectedItem() {
        return selectedItem;
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void addWordInBackground(Word word) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

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

    public void getAllWordsInBackground() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<Word> words = wordDao.getAllWords();
                allWords.postValue(words);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Reading all words from database");
                    }
                });
            }
        });
    }

    public void updateWordInBackground(Word word) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            wordDao.updateWord(word);
            handler.post(() -> {
                System.out.println("Updating word in database");
                System.out.println(word.toString());
            });
        });
    }

    public void deleteWordInBackground(Word word) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                wordDao.deleteWord(word);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Deleted word from database");
                        System.out.println(word.toString());
                    }
                });
            }
        });
    }

}
