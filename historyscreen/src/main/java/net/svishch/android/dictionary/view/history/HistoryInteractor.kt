package net.svishch.android.dictionary.view.history

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.datasource.RepositoryLocal
import net.svishch.android.dictionary.model.repository.Repository
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.viewmodel.Interactor


class HistoryInteractor(
    private val repositoryRemote: net.svishch.android.dictionary.model.repository.Repository<List<net.svishch.android.dictionary.model.repository.entity.DataModel>>,
    private val repositoryLocal: net.svishch.android.dictionary.model.datasource.RepositoryLocal<List<net.svishch.android.dictionary.model.repository.entity.DataModel>>
) : Interactor<net.svishch.android.dictionary.model.AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): net.svishch.android.dictionary.model.AppState {
        return net.svishch.android.dictionary.model.AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}
