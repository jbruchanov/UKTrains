package com.scurab.android.uktrains.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.lifecycle.ViewModelProviders
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.BoardResponse
import com.scurab.android.uktrains.net.DepartureBoardRequest
import com.scurab.android.uktrains.ui.BaseFragment
import com.scurab.android.uktrains.ui.PickStationFragment
import com.scurab.android.uktrains.util.app
import com.scurab.android.uktrains.util.npe
import kotlinx.android.synthetic.main.fragment_configure_widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ConfigureFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity())[ConfigureWidgetViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_configure_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        origin_station.setOnClickListener {
            openFragment(PickStationFragment.newInstance(PickStationFragment.TYPE_FROM))
        }

        viewModel.origin?.let { origin_station.text = it.name }
        viewModel.destination?.let { origin_station.text = it.name }
        finish.isEnabled = viewModel.origin != null

        finish.setOnClickListener {
            val activity = requireActivity()
            val appWidgetManager = AppWidgetManager.getInstance(activity)
            val views = RemoteViews(
                activity.packageName,
                R.layout.item_tain_service
            )
            val api = app().api
            api
                .getDepartureBoard(DepartureBoardRequest(viewModel.origin?.code ?: npe("Origin undefined!"), 2))
                .enqueue(object: Callback<BoardResponse> {
                    override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                        response.body()?.stationBoardResult
                            ?.let {
                                val train = it.trainServices?.first()
                                views.setTextViewText(R.id.time, "${it.stationCode} ${train?.schedTime} ${train?.estTime}")
                                views.setTextViewText(R.id.operator, train?.operator)
                                views.setTextViewText(R.id.journey, train?.journey + "\n" + Date().toGMTString())
                                // Tell the AppWidgetManager to perform an update on the current app widget
                                appWidgetManager.updateAppWidget(viewModel.widgetId, views)
                                //done
                                val resultValue = Intent()
                                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, viewModel.widgetId)
                                activity.setResult(Activity.RESULT_OK, resultValue)
                                requireActivity().finish()
                            }
                    }

                    override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                        views.setTextViewText(R.id.time, "")
                        views.setTextViewText(R.id.operator, "")
                        views.setTextViewText(R.id.journey, t.message)
                    }
                })

            val list = (sharedPrefs.widgets as MutableList?) ?: mutableListOf()
            list.add(viewModel)
            sharedPrefs.widgets = list
        }
    }
}