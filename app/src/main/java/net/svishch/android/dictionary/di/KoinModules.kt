package net.svishch.android.dictionary.di

import androidx.room.Room
import net.svishch.android.dictionary.model.datasource.RepositoryLocal
import net.svishch.android.dictionary.model.datasource.RoomDataBaseImplementation
import net.svishch.android.dictionary.model.repository.Repository
import net.svishch.android.dictionary.model.repository.RepositoryImplementation
import net.svishch.android.dictionary.model.repository.RepositoryImplementationLocal
import net.svishch.android.dictionary.model.repository.RetrofitImplementation
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.room.HistoryDataBase
import net.svishch.android.dictionary.view.main.MainInteractor
import net.svishch.android.dictionary.view.main.MainViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectDependencies() = loadModules

private val loadModules by lazy {
    loadKoinModules(listOf(application, mainScreen))
}

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }

    single<Repository<List<DataModel>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<DataModel>>> { RepositoryImplementationLocal(RoomDataBaseImplementation(get()))}
}

val mainScreen = module {
    factory { MainViewModel(get()) }
    factory { MainInteractor(get(), get()) }
}
