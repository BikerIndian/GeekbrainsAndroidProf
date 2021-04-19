package net.svishch.android.dictionary.view

import net.svishch.android.dictionary.model.AppState

interface View {

    fun renderData(appState: AppState)

}
