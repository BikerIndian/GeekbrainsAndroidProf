package net.svishch.android.dictionary.model.datasource

import net.svishch.android.dictionary.model.repository.entity.DataModel
import io.reactivex.Observable
import net.svishch.android.dictionary.model.repository.RetrofitImplementation

class DataSourceRemote(private val remoteProvider: RetrofitImplementation = RetrofitImplementation()) :
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}
