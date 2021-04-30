package net.svishch.android.dictionary.model.datasource

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.repository.Repository


interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
}
