package net.svishch.android.dictionary.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import net.svishch.android.dictionary.view.main.MainActivity
import javax.inject.Singleton

@Component(
    modules = [
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContent(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)
}
