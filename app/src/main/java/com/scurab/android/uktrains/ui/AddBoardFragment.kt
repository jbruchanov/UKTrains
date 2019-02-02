package com.scurab.android.uktrains.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.model.StationBoard
import com.scurab.android.uktrains.model.TrainStation
import com.scurab.android.uktrains.util.app
import com.scurab.android.uktrains.util.npe
import kotlinx.android.synthetic.main.fragment_add_board.*

private const val STATION_BOARD = "STATION_BOARD"

class AddBoardFragment : BaseFragment(), PickStationFragmentResultListener {

    private var stationBoard: StationBoard? = null

    override fun onPickStationResult(type: Int, trainStation: TrainStation) {
        stationBoard?.let {
            when (type) {
                PickStationFragment.TYPE_FROM -> it.origin = trainStation
                PickStationFragment.TYPE_TO -> it.destination = trainStation
            }
        } ?: npe("StationBoard is not initialized!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationBoard = savedInstanceState?.let {
            it.getSerializable(STATION_BOARD) as StationBoard
        } ?: stationBoard ?: StationBoard()

        origin_station.setOnClickListener {
            showPickStationFragment(PickStationFragment.TYPE_FROM)
        }

        destination_station.setOnClickListener {
            showPickStationFragment(PickStationFragment.TYPE_TO)
        }

        stationBoard?.origin?.let { origin_station.text = it.name }
        stationBoard?.destination?.let { destination_station.text = it.name }
        finish.isEnabled = stationBoard?.origin != null
        finish.setOnClickListener {
            stationBoard?.let { board ->
                app().sharedPrefs.let { prefs ->
                    var stations = (prefs.stationBoards ?: mutableListOf()).toMutableList()
                    stations.add(board)
                    prefs.stationBoards = stations
                    popBackStack()
                }
            } ?: Toast.makeText(requireContext(), "No board?!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showPickStationFragment(type: Int) {
        PickStationFragment.newInstance(type).apply {
            setTargetFragment(this@AddBoardFragment, 1)
            showFragment(this, true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(STATION_BOARD, stationBoard)
    }
}