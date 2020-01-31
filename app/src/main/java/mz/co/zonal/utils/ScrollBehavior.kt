package mz.co.zonal.utils

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ScrollBehavior : CoordinatorLayout.Behavior<MaterialButton> {
    private val valueAnimator = ValueAnimator()
    private var expandWidth = 0
    private var collapseWidth = 0

    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    )

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: MaterialButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: MaterialButton,
        dependency: View
    ): Boolean {
        if (expandWidth == 0 && child.measuredWidth != 0) {
            expandWidth = child.measuredWidth
        }
        if (collapseWidth == 0 && child.minWidth != 0) {
            collapseWidth = child.minWidth
        }
        return dependency is RecyclerView
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: MaterialButton, target: View, dxConsumed: Int,
        dyConsumed: Int, dxUnconsumed: Int, dy: Int
    ) {
        if (dyConsumed < 0) { //Scrolling down
            child.visibility = View.INVISIBLE
        } else if (dyConsumed > 0) { // Scrolling up
            child.visibility = View.VISIBLE
        }
    }

    private fun measure(v: View, duration: Int, width: Int) {
        if (valueAnimator.isRunning) {
            return
        }
        val preWidth = v.measuredWidth
        valueAnimator.setIntValues(preWidth, width)
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.width = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.start()
    }
}