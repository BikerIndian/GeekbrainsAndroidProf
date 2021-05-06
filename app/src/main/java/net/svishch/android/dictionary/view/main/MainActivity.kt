package net.svishch.android.dictionary.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_main.*
import net.svishch.android.dictionary.R
import net.svishch.android.dictionary.di.injectDependencies
import net.svishch.android.dictionary.model.AppState
import net.svishch.android.dictionary.model.network.convertMeaningsToString
import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.utils.isOnline
import net.svishch.android.dictionary.utils.ui.viewById
import net.svishch.android.dictionary.view.BaseActivity
import net.svishch.android.dictionary.view.descriptionscreen.DescriptionActivity
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

private const val HISTORY_ACTIVITY_PATH =
    "net.svishch.android.dictionary.view.history.HistoryActivity"
private const val HISTORY_ACTIVITY_FEATURE_NAME = "historyscreen"
private const val REQUEST_CODE = 42

class MainActivity : BaseActivity<AppState, MainInteractor>() {


    override lateinit var model: MainViewModel

    private lateinit var splitInstallManager: SplitInstallManager

    private val mainActivityRecyclerView by viewById<RecyclerView>(R.id.main_activity_recyclerview)
    private val searchEditText by viewById<TextInputEditText>(R.id.input_edit_text)


    // Для обновления
    private lateinit var appUpdateManager: AppUpdateManager

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
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


    private val stateUpdatedListener: InstallStateUpdatedListener =
        InstallStateUpdatedListener { state ->
            state?.let {
                if (it.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniViewModel()
        initViews()
        initClickListener()
        checkForUpdates()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        REQUEST_CODE
                    )
                }
            }
    }

    private fun initClickListener() {
        input_layout.setEndIconOnClickListener {
            model.getData(searchEditText.text.toString(), isNetworkAvailable)
        }

        // Отработка нажатия ENTER
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            println(actionId)
            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    model.getData(searchEditText.text.toString(), isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val data = appState.data
                if (data.isNullOrEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    adapter.setData(data)
                }
            }
            is AppState.Loading -> {
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
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun iniViewModel() {
        if (mainActivityRecyclerView.adapter != null) {
            throw IllegalStateException(getString(R.string.activity_exception))
        }
        injectDependencies()
        val viewModel: MainViewModel by currentScope.inject()
        model = viewModel
        model.subscribe().observe(this@MainActivity, { renderData(it) })
    }

    private fun initViews() {
        mainActivityRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mainActivityRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                // startActivity(Intent(this, HistoryActivity::class.java))
                loadFeature()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun loadFeature() {
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)

        // Создаём запрос на создание экрана
        val request =
            SplitInstallRequest
                .newBuilder()
                .addModule(HISTORY_ACTIVITY_FEATURE_NAME)
                .build()

        splitInstallManager
            .startInstall(request)
            // Добавляем слушатель в случае успеха
            .addOnSuccessListener {
                // Открываем экран
                val intent = Intent().setClassName(packageName, HISTORY_ACTIVITY_PATH)
                startActivity(intent)
            }
            // Добавляем слушатель в случае, если что-то пошло не так
            .addOnFailureListener {
                // Обрабатываем ошибку
                Toast.makeText(
                    applicationContext,
                    "Couldn't download feature: " + it.message,
                    Toast.LENGTH_LONG
                ).show()
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

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    // Сам метод вызываем в onCreate
    private fun checkForUpdates() {
        // Создаём менеджер
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        // Возвращает интент (appUpdateInfo), который мы будем использовать
        // в качестве информации для обновления
        val appUpdateInfo = appUpdateManager.appUpdateInfo
        // Проверяем наличие обновления
        appUpdateInfo.addOnSuccessListener { appUpdateIntent ->
            if (appUpdateIntent.updateAvailability() ==
                UpdateAvailability.UPDATE_AVAILABLE
                // Здесь мы делаем проверку на немедленный тип обновления
                // (IMMEDIATE); для гибкого нужно передавать AppUpdateType.FLEXIBLE
                && appUpdateIntent.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                // Передаём слушатель прогресса (только для гибкого типа
                // обновления)

                appUpdateManager.registerListener(stateUpdatedListener)
                // Выполняем запрос
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateIntent,
                    IMMEDIATE,
                    this,
                    // Реквест-код для обработки запроса в onActivityResult
                    REQUEST_CODE
                )
            }
        }
    }



    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.activity_main_layout),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }
}
