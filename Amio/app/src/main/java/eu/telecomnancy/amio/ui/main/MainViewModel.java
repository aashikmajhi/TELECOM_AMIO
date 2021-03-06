package eu.telecomnancy.amio.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import eu.telecomnancy.amio.iotlab.models.Mote;

/**
 * Using a ViewModel avoid the service to be stopped and restarted wen the device orientation change
 * (or any other thing that refresh the UI)
 */
public class MainViewModel extends ViewModel {

    /**
     * Mote list able to register observers
     */
    private final MutableLiveData<List<Mote>> _motes = new MutableLiveData<>();

    /**
     * Getter fot the mote list
     *
     * @return the mote list
     */
    public LiveData<List<Mote>> getMotes() {
        return _motes;
    }

    /**
     * Update the content of the live data model
     *
     * @param newMotes New list of mote to replace the currently displayed one
     */
    public void setMotes(List<Mote> newMotes) {
        //postValue can be used in a background task and will trigger all observers
        _motes.postValue(newMotes);
    }

}
