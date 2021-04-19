package net.svishch.android.dictionary.view

import androidx.appcompat.app.AppCompatActivity
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.presenter.Presenter

abstract class BaseActivity<T : AppState> : AppCompatActivity(), View {

    protected val presenter: Presenter<T, View> by lazy {
        createPresenter()
    }

    protected abstract fun createPresenter(): Presenter<T, View>

    abstract override fun renderData(appState: AppState)

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }
}
