package com.example.mycurrencyconverter.util.viewUtil

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author : Vivek Singh
 * @param spanCount: number of columns in a Recyclerview Grid
 */
class GridSpacingItemDecoration(private val spanCount: Int) : RecyclerView.ItemDecoration() {

    private fun Int.toDp() = (density * this).toInt()
    private val density = Resources.getSystem().displayMetrics.density
    private val spacing = 8.toDp()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount
        if (position < spanCount) {
            outRect.top = spacing
        }
        outRect.bottom = spacing
    }
}