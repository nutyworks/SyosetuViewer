package me.nutyworks.syosetuviewerv2.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.adapter.NovelListAdapter

class SwipeToDeleteCallback(private val mAdapter: NovelListAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val mIcon =
        ContextCompat.getDrawable(mAdapter.context, R.drawable.ic_baseline_delete_forever_24)!!
    private val mBackground = ColorDrawable(Color.RED)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        mAdapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconMargin: Int = (itemView.height - mIcon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - mIcon.intrinsicHeight) / 2
        val iconBottom: Int = iconTop + mIcon.intrinsicHeight

        when {
            dX < 0 -> { // Swiping to the left
                val iconLeft: Int = itemView.right - iconMargin - mIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                mBackground.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> { // view is unSwiped
                mBackground.setBounds(0, 0, 0, 0)
                mIcon.setBounds(0, 0, 0, 0)
            }
        }

        mBackground.draw(c)
        mIcon.draw(c)
    }
}