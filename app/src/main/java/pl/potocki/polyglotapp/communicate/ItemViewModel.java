package pl.potocki.polyglotapp.communicate;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.potocki.polyglotapp.database.WordDao;
import pl.potocki.polyglotapp.model.language.SelectedLanguages;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<SelectedLanguages> selectedItem = new MutableLiveData<>();
    private WordDao wordDao;

    public void setData(SelectedLanguages selectedLanguages) {
        selectedItem.setValue(selectedLanguages);
    }

    public LiveData<SelectedLanguages> getSelectedItem() {
        return selectedItem;
    }



}
