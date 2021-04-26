package net.svishch.android.dictionary.model.repository

import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.datasource.DataSource
import io.reactivex.Observable

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {
    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}
