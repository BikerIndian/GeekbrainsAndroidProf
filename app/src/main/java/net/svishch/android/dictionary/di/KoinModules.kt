package net.svishch.android.dictionary.di

import androidx.room.Room
import net.svishch.android.dictionary.model.datasource.RoomDataBaseImplementation
import net.svishch.android.dictionary.model.repository.Repository
import net.svishch.android.dictionary.model.repository.RepositoryImplementation
import net.svishch.android.dictionary.model.repository.RetrofitImplementation
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.room.HistoryDataBase
import net.svishch.android.dictionary.view.main.MainInteractor
import net.svishch.android.dictionary.view.main.MainViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }

    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) { RepositoryImplementation(
        RetrofitImplementation()
    ) }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) { RepositoryImplementation(
        RoomDataBaseImplementation()
    ) }
}

val mainScreen = module {
    factory { MainInteractor(get(named(NAME_REMOTE)), get(named(NAME_LOCAL))) }
    factory { MainViewModel(get()) }
}
