package net.svishch.android.dictionary.view.history

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import net.svishch.android.dictionary.utils.ui.parseLocalSearchResults
import net.svishch.android.dictionary.viewmodel.BaseViewModel

class HistoryViewModel(private val interactor: HistoryInteractor) :
    BaseViewModel<net.svishch.android.dictionary.model.AppState>() {

    private val liveDataForViewToObserve: LiveData<net.svishch.android.dictionary.model.AppState> = _mutableLiveData

    fun subscribe(): LiveData<net.svishch.android.dictionary.model.AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean) {
        _mutableLiveData.value = net.svishch.android.dictionary.model.AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
    }

    private suspend fun startInteractor(word: String, isOnline: Boolean) {
        _mutableLiveData.postValue(parseLocalSearchResults(interactor.getData(word, isOnline)))
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(net.svishch.android.dictionary.model.AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = net.svishch.android.dictionary.model.AppState.Success(null)//Set View to original state in onStop
        super.onCleared()
    }
}
