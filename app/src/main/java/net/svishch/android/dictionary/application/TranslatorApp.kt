package net.svishch.android.dictionary.application


import android.app.Application
import net.svishch.android.dictionary.di.application
import net.svishch.android.dictionary.di.historyScreen
import net.svishch.android.dictionary.di.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class TranslatorApp : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, mainScreen, historyScreen))
        }
    }

}

