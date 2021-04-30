package net.svishch.android.dictionary.model.room

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.datasource.DataSource

interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}
