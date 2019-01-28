package com.scurab.android.uktrains.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.model.TrainStation
import com.scurab.android.uktrains.widget.textTextWithBoldLetters
import kotlinx.android.synthetic.main.fragment_pick_station.*
import java.nio.charset.Charset

class PickStationFragment : BaseFragment() {

    private lateinit var pickStationAdapter: PickStationAdapter
    private var trainStations: List<TrainStation>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pick_station, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trainStations = resources.assets.open("station_codes.csv").use { stream ->
            String(stream.readBytes(), Charset.defaultCharset())
                .lines()
                .map { TrainStation.fromCSV(it) }
        }

        pickStationAdapter = PickStationAdapter()
        pickStationAdapter.items = trainStations
        recyclerview.adapter = pickStationAdapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        name_station.editText?.addTextChangedListener {
            pickStationAdapter.filterExpr = it.toString()
        }
    }
}

class PickStationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val emptyIntArray = intArrayOf()

    var filterExpr: String? = null
        set(value) {
            val subValue = value?.trim()?.toLowerCase()
            if (subValue != field) {
                field = subValue
                refreshFilter()
            }
        }

    var items: List<TrainStation>? = null
        set(value) {
            field = value
            refreshFilter()
        }

    private var filteredItems: List<Pair<TrainStation, IntArray>>? = null

    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    private fun refreshFilter() {
        filterExpr.let { expr ->
            items?.let { list ->
                val result = mutableListOf<Pair<TrainStation, IntArray>>()
                list.forEach { ts ->
                    if (expr != null && expr.isNotEmpty()) {
                        ts.matchingIndexes(expr)
                            .takeIf { it.isNotEmpty() }
                            ?.let { result.add(Pair(ts, it)) }
                    } else {
                        result.add(Pair(ts, emptyIntArray))
                    }
                }
                //if we have a station matching code, just move it to top position
                if (expr?.length == 3) {
                    result
                        .find { it.first.codeLowerCase == expr }
                        ?.let {
                            result.remove(it)
                            result.add(0, it)
                        }
                }
                filteredItems = result
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_station, parent, false)) {}
    }

    override fun getItemCount(): Int {
        return filteredItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        filteredItems?.get(position)?.let { (ts, indexes) ->
            (holder.itemView as? TextView)?.textTextWithBoldLetters("${ts.name} [${ts.code}]" , indexes)
        }
    }
}