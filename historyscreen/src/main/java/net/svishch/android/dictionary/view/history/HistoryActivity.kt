package net.svishch.android.dictionary.view.history

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_history.*

import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.view.BaseActivity
import net.svishch.android.dictionary.view.injectDependencies
import org.koin.android.viewmodel.ext.android.viewModel


class HistoryActivity : BaseActivity<AppState, HistoryInteractor>() {

    override lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter(onListItemClickListener) }

    private val onListItemClickListener: HistoryAdapter.OnListItemClickListener =
        object : HistoryAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@HistoryActivity, "on click: ${data.text}", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setActionbarHomeButtonAsUp()
        iniViewModel()
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {

        super.onResume()
        model.getData("", false)
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        if (history_activity_recyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        injectDependencies()
        val viewModel: HistoryViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@HistoryActivity, Observer<AppState> { renderData(it) })
    }

    private fun initViews() {
        history_activity_recyclerview.adapter = adapter
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val data = appState.data
                if (!data.isNullOrEmpty()) {
                    adapter.setData(data)
                }
            }
        }
    }

    private fun setActionbarHomeButtonAsUp() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
