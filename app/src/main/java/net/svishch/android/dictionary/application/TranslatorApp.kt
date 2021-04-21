package net.svishch.android.dictionary.application


import android.app.Application
import net.svishch.android.dictionary.di.AppComponent
import net.svishch.android.dictionary.di.DaggerAppComponent


class TranslatorApp : Application(){

    override fun onCreate() {
        super.onCreate()
        val component = DaggerAppComponent.builder().appContent(this).build()
        TranslatorApp.component = component
    }

    companion object {
        lateinit var  component: AppComponent
    }
}

