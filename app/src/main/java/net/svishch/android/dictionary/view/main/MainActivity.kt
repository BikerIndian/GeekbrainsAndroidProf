package net.svishch.android.dictionary.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import net.svishch.android.dictionary.R
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.network.convertMeaningsToString
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.utils.isOnline
import net.svishch.android.dictionary.view.BaseActivity
import net.svishch.android.dictionary.view.descriptionscreen.DescriptionActivity
import net.svishch.android.dictionary.view.history.HistoryActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<net.svishch.android.dictionary.model.AppState, MainInteractor>() {

    override lateinit var model: MainViewModel

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: net.svishch.android.dictionary.model.repository.entity.DataModel) {
                startActivity(
                    DescriptionActivity.getIntent(
                        this@MainActivity,
                        data.text!!,
                        convertMeaningsToString(data.meanings!!),
                        data.meanings!![0].imageUrl
                    )
                )
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniViewModel()
        initViews()
        initClickListener()
    }

    private fun initClickListener() {
        input_layout.setEndIconOnClickListener {
            model.getData(input_edit_text.text.toString(), isNetworkAvailable)
        }

        // Отработка нажатия ENTER
        input_edit_text.setOnEditorActionListener { v, actionId, event ->
            println(actionId)
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    model.getData(input_edit_text.text.toString(), isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun renderData(appState: net.svishch.android.dictionary.model.AppState) {
        when (appState) {
            is net.svishch.android.dictionary.model.AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    adapter.setData(data)
                }
            }
            is net.svishch.android.dictionary.model.AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    progress_bar_horizontal.visibility = VISIBLE
                    progress_bar_round.visibility = GONE
                    progress_bar_horizontal.progress = appState.progress!!
                } else {
                    progress_bar_horizontal.visibility = GONE
                    progress_bar_round.visibility = VISIBLE
                }
            }
            is net.svishch.android.dictionary.model.AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun iniViewModel() {
        if (main_activity_recyclerview.adapter != null) {
            throw IllegalStateException(getString(R.string.activity_exception))
        }
        val viewModel: MainViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@MainActivity, Observer<net.svishch.android.dictionary.model.AppState> { renderData(it) })
    }

    private fun initViews() {
        main_activity_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        main_activity_recyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        error_textview.text = error ?: getString(R.string.undefined_error)
        reload_button.setOnClickListener {
            model.getData("hi", isNetworkAvailable)
        }
    }

    private fun showViewSuccess() {
        loading_frame_layout.visibility = GONE
        error_linear_layout.visibility = GONE
    }

    private fun showViewLoading() {
        loading_frame_layout.visibility = VISIBLE
        error_linear_layout.visibility = GONE
    }

    private fun showViewError() {
        loading_frame_layout.visibility = GONE
        error_linear_layout.visibility = VISIBLE
    }

    private fun showViewWorking() {
        loading_frame_layout.visibility = GONE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }

    override fun setDataToAdapter(data: List<net.svishch.android.dictionary.model.repository.entity.DataModel>) {
        adapter.setData(data)
    }
}
