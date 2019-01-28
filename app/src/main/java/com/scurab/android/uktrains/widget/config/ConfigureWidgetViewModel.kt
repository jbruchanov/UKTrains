package com.scurab.android.uktrains.widget.config

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.scurab.android.uktrains.model.TrainStation

class ConfigureWidgetViewModel() : ViewModel(), Parcelable {
    var widgetId: Int = 0
    var origin: TrainStation? = null
    var destination: TrainStation? = null

    constructor(source: Parcel) : this() {
        source.apply {
            widgetId = source.readInt()
            val originName = source.readString()
            val originCode = source.readString()
            val destName = source.readString()
            val destCode = source.readString()
            if (originName != null && originCode != null) {
                origin = TrainStation(originName, originCode)
            }
            if (destName != null && destCode != null) {
                destination = TrainStation(destName, destCode)
            }
        }
    }

    operator fun component1() : TrainStation? = origin
    operator fun component2() : TrainStation? = destination

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(widgetId)
        writeString(origin?.name)
        writeString(origin?.code)
        writeString(destination?.name)
        writeString(destination?.code)
    }

    fun read(viewModel: ConfigureWidgetViewModel?) {
        widgetId = viewModel?.widgetId ?: 0
        origin = viewModel?.origin
        destination = viewModel?.destination
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ConfigureWidgetViewModel> =
            object : Parcelable.Creator<ConfigureWidgetViewModel> {
                override fun createFromParcel(source: Parcel): ConfigureWidgetViewModel =
                    ConfigureWidgetViewModel(source)

                override fun newArray(size: Int): Array<ConfigureWidgetViewModel?> = arrayOfNulls(size)
            }
    }
}