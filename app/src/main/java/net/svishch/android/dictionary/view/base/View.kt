package net.svishch.android.dictionary.view.base

import net.svishch.android.dictionary.model.data.AppState

interface View {

    fun renderData(appState: AppState)

}
