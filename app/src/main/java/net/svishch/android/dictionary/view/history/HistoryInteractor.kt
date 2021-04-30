package net.svishch.android.dictionary.view.history

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.datasource.RepositoryLocal
import net.svishch.android.dictionary.model.repository.Repository
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.viewmodel.Interactor


class HistoryInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            if (fromRemoteSource) {
                repositoryRemote
            } else {
                repositoryLocal
            }.getData(word)
        )
    }
}
