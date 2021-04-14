package net.svishch.android.dictionary.presenter

import net.svishch.android.dictionary.model.data.AppState
import net.svishch.android.dictionary.view.base.View

interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)

    fun detachView(view: V)

    fun getData(word: String, isOnline: Boolean)
}
