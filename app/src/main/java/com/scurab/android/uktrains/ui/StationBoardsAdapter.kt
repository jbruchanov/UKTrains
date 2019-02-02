package com.scurab.android.uktrains.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scurab.android.uktrains.R
import com.scurab.android.uktrains.net.StationBoardResult
import com.scurab.android.uktrains.net.names
import com.scurab.android.uktrains.util.findViehHolderPosition
import com.scurab.android.uktrains.util.ise
import com.scurab.android.uktrains.util.npe

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1
private const val TYPE_FOOTER = 2

class StationBoardsAdapter : RecyclerView.Adapter<StationViewHolder>() {

    var removeBoardClickListener: ((id: Long, board: StationBoardResult) -> Unit)? = null

    private val items = mutableListOf<Item>()
    private var layoutInflater: LayoutInflater? = null
    private var itemPadding: Int = 0

    private val innerCloseClickListener = View.OnClickListener { v ->
        removeBoardClickListener?.let { listener ->
            items[v.findViehHolderPosition()].board.let {
                listener.invoke(it.saveId ?: ise("SaveId must have a value"), it)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
        itemPadding = recyclerView.context.resources.getDimensionPixelSize(R.dimen.gap_large)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val li = layoutInflater ?: npe("Null LayoutInflater?")
        return when (viewType) {
            TYPE_HEADER -> StationViewHolder(li.inflate(R.layout.item_station_board_header, parent, false)).apply {
                itemView.findViewById<View>(R.id.remove).setOnClickListener(innerCloseClickListener)
            }
            TYPE_ITEM,
            TYPE_FOOTER -> StationViewHolder(li.inflate(R.layout.item_station_board_item, parent, false))
            else -> ise("Unhandled viewType:$viewType")
        }

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val item = items[position]
        val drawableId = when (item.type) {
            TYPE_HEADER -> 0
            TYPE_FOOTER -> R.drawable.background_card_bottom
            else -> R.drawable.background_card_mid
        }

        when (item.type) {
            TYPE_HEADER -> {
                holder.apply {
                    itemView.setPadding(itemPadding, itemPadding / 2, itemPadding / 2, 0)
                    textView.setPadding(0, 0, 0, 0)
                }
            }
            TYPE_FOOTER -> {
                holder.itemView.apply {
                    background = resources.getDrawable(R.drawable.background_card_bottom)
                    setPadding(itemPadding, 0, itemPadding, itemPadding)
                }
            }
            else -> {
                holder.itemView.apply {
                    background = resources.getDrawable(R.drawable.background_card_mid)
                    setPadding(itemPadding, 0, itemPadding, 0)
                }
            }
        }

        holder.textView?.apply {
            text = item.title
        }
    }

    fun setItems(boards: Collection<StationBoardResult>) {
        items.clear()
        boards.forEach { board ->
            addItem(board, false)
        }
        notifyDataSetChanged()
    }

    fun removeItem(board: StationBoardResult) {
        val newItems = items.toMutableList()
        newItems.removeAll(items.filter { it.board == board })
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return newItems[newItemPosition] === items[oldItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return newItems[newItemPosition] === items[oldItemPosition]
            }
        }).apply {
            items.clear()
            items.addAll(newItems)
            dispatchUpdatesTo(this@StationBoardsAdapter)
        }
        notifyDataSetChanged()
    }

    fun addItem(board: StationBoardResult) {
        addItem(board, true)
    }

    private fun addItem(board: StationBoardResult, notify: Boolean = false) {
        items.add(Item(TYPE_HEADER, board.locationName, board))
        board.trainServices?.forEach { ts ->
            items.add(Item(TYPE_ITEM, "${ts.schedTime} ${ts.destination.names()}", board))
        }
        if (board.trainServices?.isEmpty() != false) {
            items.add(Item(TYPE_FOOTER, "", board))
        }
        items.last().takeIf { it.type == TYPE_ITEM }
            ?.let { it.type = TYPE_FOOTER }
        if (notify) {
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }
}

class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.title)
}

private data class Item(var type: Int, val title: String, val board: StationBoardResult) {

}