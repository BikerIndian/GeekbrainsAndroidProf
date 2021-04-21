package net.svishch.android.dictionary.di

import dagger.Module
import dagger.Provides
import net.svishch.android.dictionary.model.datasource.DataSource
import net.svishch.android.dictionary.model.datasource.RoomDataBaseImplementation
import net.svishch.android.dictionary.model.repository.Repository
import net.svishch.android.dictionary.model.repository.RepositoryImplementation
import net.svishch.android.dictionary.model.repository.RetrofitImplementation
import net.svishch.android.dictionary.model.repository.entity.DataModel
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideRepositoryRemote(@Named(NAME_REMOTE) dataSourceRemote: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImplementation(dataSourceRemote)

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideRepositoryLocal(@Named(NAME_LOCAL) dataSourceLocal: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImplementation(dataSourceLocal)

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideDataSourceRemote(): DataSource<List<DataModel>> =
        RetrofitImplementation()

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideDataSourceLocal(): DataSource<List<DataModel>> = RoomDataBaseImplementation()
}
