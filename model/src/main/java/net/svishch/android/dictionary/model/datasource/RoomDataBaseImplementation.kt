package net.svishch.android.dictionary.model.datasource

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.room.DataSourceLocal
import net.svishch.android.dictionary.model.room.HistoryDao
import net.svishch.android.dictionary.utils.ui.convertDataModelSuccessToEntity
import net.svishch.android.dictionary.utils.ui.mapHistoryEntityToSearchResult

class RoomDataBaseImplementation(private val historyDao: HistoryDao) :
    DataSourceLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {
        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}