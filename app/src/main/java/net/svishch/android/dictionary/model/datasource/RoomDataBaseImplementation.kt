package net.svishch.android.dictionary.model.datasource

import net.svishch.android.dictionary.model.data.DataModel
import io.reactivex.Observable
import net.svishch.android.dictionary.model.datasource.DataSource

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
