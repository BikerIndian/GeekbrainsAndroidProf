package net.svishch.android.dictionary.view.main

import net.svishch.android.dictionary.viewmodel.Interactor
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.repository.Repository


class MainInteractor(
    private val repositoryRemote: Repository<List<DataModel>>,
    private val repositoryLocal: Repository<List<DataModel>>
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
