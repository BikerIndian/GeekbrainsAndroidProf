package net.svishch.android.dictionary.model.repository

import net.svishch.android.dictionary.model.data.DataModel
import net.svishch.android.dictionary.model.datasource.DataSource
import io.reactivex.Observable
import net.svishch.android.dictionary.model.repository.Repository

class RepositoryImplementation(private val dataSource: DataSource<List<DataModel>>) :
    Repository<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        return dataSource.getData(word)
    }
}
