package net.svishch.android.dictionary.model.repository

import net.svishch.android.dictionary.model.repository.entity.DataModel

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("words/search")
    fun searchAsync(@Query("search") wordToSearch: String): Deferred<List<DataModel>>

}
