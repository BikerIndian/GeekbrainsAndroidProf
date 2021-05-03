package net.svishch.android.dictionary.view

import net.svishch.android.dictionary.view.history.HistoryInteractor
import net.svishch.android.dictionary.view.history.HistoryViewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectDependencies() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(historyScreen))
}

val historyScreen = module {
    factory { HistoryViewModel(get()) }
    factory { HistoryInteractor(get(), get()) }
}
