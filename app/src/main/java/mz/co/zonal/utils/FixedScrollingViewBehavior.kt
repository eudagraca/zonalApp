package mz.co.zonal.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout


class FixedScrollingViewBehavior : AppBarLayout.ScrollingViewBehavior {
    constructor()
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        if (child.layoutParams.height == -1) {
            val dependencies: List<*> = parent.getDependencies(child)
            if (dependencies.isEmpty()) {
                return false
            }
            val appBar: AppBarLayout? =
                findFirstAppBarLayout(dependencies as List<View>)
            if (appBar != null && ViewCompat.isLaidOut(appBar)) {
                if (ViewCompat.getFitsSystemWindows(appBar)) {
                    ViewCompat.setFitsSystemWindows(child, true)
                }
                val scrollRange: Int = appBar.totalScrollRange
                val height: Int =
                    parent.height - appBar.measuredHeight + Math.min(
                        scrollRange,
                        parent.height - heightUsed
                    )
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    height,
                    View.MeasureSpec.EXACTLY
                )
                parent.onMeasureChild(
                    child,
                    parentWidthMeasureSpec,
                    widthUsed,
                    heightMeasureSpec,
                    heightUsed
                )
                return true
            }
        }
        return false
    }

    companion object {
        private fun findFirstAppBarLayout(views: List<View>): AppBarLayout? {
            var i = 0
            val z = views.size
            while (i < z) {
                val view = views[i]
                if (view is AppBarLayout) {
                    return view
                }
                ++i
            }
            return null
        }
    }
}