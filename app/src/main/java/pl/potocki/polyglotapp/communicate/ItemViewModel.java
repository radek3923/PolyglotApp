package pl.potocki.polyglotapp.communicate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.potocki.polyglotapp.model.language.SelectedLanguages;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<SelectedLanguages> selectedItem = new MutableLiveData<>();

    public void setData(SelectedLanguages selectedLanguages) {
        selectedItem.setValue(selectedLanguages);
    }

    public LiveData<SelectedLanguages> getSelectedItem() {
        return selectedItem;
    }
}
