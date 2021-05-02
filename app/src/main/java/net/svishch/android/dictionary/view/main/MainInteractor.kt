package net.svishch.android.dictionary.view.main

import net.svishch.android.dictionary.viewmodel.Interactor
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.datasource.RepositoryLocal
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.repository.Repository


class MainInteractor(
    private val repositoryRemote: net.svishch.android.dictionary.model.repository.Repository<List<net.svishch.android.dictionary.model.repository.entity.DataModel>>,
    private val repositoryLocal: net.svishch.android.dictionary.model.datasource.RepositoryLocal<List<net.svishch.android.dictionary.model.repository.entity.DataModel>>
) : Interactor<net.svishch.android.dictionary.model.AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): net.svishch.android.dictionary.model.AppState {
        val appState: net.svishch.android.dictionary.model.AppState
        if (fromRemoteSource) {
            appState = net.svishch.android.dictionary.model.AppState.Success(repositoryRemote.getData(word))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = net.svishch.android.dictionary.model.AppState.Success(repositoryLocal.getData(word))
        }
        return appState
    }
}
