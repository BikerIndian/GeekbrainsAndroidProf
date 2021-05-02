package net.svishch.android.dictionary.model.repository

import net.svishch.android.dictionary.model.repository.entity.DataModel

class RepositoryImplementation(private val dataSource: net.svishch.android.dictionary.model.datasource.DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {
    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }
}
