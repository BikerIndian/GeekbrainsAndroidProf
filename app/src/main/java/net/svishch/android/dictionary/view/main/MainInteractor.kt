package net.svishch.android.dictionary.view.main

import net.svishch.android.dictionary.presenter.Interactor
import net.svishch.android.dictionary.model.data.AppState
import net.svishch.android.dictionary.model.data.DataModel
import net.svishch.android.dictionary.model.repository.Repository
import io.reactivex.Observable

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map { AppState.Success(it) }
        } else {
            localRepository.getData(word).map { AppState.Success(it) }
        }
    }
}
