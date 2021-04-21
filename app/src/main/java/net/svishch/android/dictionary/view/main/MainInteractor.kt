package net.svishch.android.dictionary.view.main

import net.svishch.android.dictionary.viewmodel.Interactor
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.repository.Repository
import io.reactivex.Observable
import net.svishch.android.dictionary.di.NAME_LOCAL
import net.svishch.android.dictionary.di.NAME_REMOTE
import javax.inject.Named

class MainInteractor(
    @Named(NAME_REMOTE) val repositoryRemote: Repository<List<DataModel>>,
    @Named(NAME_LOCAL) val repositoryLocal: Repository<List<DataModel>>
) : Interactor<AppState> {

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        return if (fromRemoteSource) {
            repositoryRemote
        } else {
            repositoryLocal
        }.getData(word).map { AppState.Success(it) }
    }
}
