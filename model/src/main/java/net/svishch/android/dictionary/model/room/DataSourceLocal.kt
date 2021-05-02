package net.svishch.android.dictionary.model.room

import net.svishch.android.dictionary.model.AppState

interface DataSourceLocal<T> : net.svishch.android.dictionary.model.datasource.DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}
