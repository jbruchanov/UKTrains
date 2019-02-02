package com.scurab.android.uktrains.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.DepartureBoardRequest
import com.scurab.android.uktrains.net.StationBoardResult
import com.scurab.android.uktrains.util.app
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*

class MainFragment : BaseFragment() {

    private val sharedPrefs by lazy { app().sharedPrefs }
    private val adapter = StationBoardsAdapter()
    private var forceReload = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floating_action_button.setOnClickListener {
            showFragment(AddBoardFragment(), true)
            forceReload = true
        }
        adapter.removeBoardClickListener = { saveId, item ->
            sharedPrefs.stationBoards?.toMutableList()?.let { items ->
                items
                    .indexOfFirst { it.id == saveId }
                    ?.let {
                        items.removeAt(it)
                        sharedPrefs.stationBoards = items
                        adapter.removeItem(item)
                    }
            }
        }
        recyclerview.apply {
            adapter = this@MainFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        swipe_refresh.setOnRefreshListener {
            reload()
        }
    }

    override fun onStart() {
        super.onStart()
        if (forceReload || adapter.itemCount == 0) {
            reload()
            forceReload = false
        }
    }

    private fun reload() = uiScope.launch {
        swipe_refresh.isRefreshing = true
        val task = async { load() }
        adapter.setItems(task.await())
        swipe_refresh.isRefreshing = false
    }

    private suspend fun load(): MutableList<StationBoardResult> {
        val result = mutableListOf<StationBoardResult>()
        withContext(ioScope) {
            sharedPrefs.stationBoards?.forEach { stationBoard ->
                api
                    .getDepartureBoardAsync(stationBoard.toRequest())
                    .await()
                    .stationBoardResult
                    ?.let {
                        it.saveId = stationBoard.id
                        result.add(it)
                    }
            }
        }
        return result
    }
}