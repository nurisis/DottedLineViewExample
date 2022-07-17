package com.hinuri.dottedlineexample.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hinuri.dottedlineexample.R


class DottedLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        const val ORIENTATION_HORIZONTAL = 0
        const val ORIENTATION_VERTICAL = 1
    }

    private val paint = Paint()
    private val path = Path()
    private var orientation = ORIENTATION_HORIZONTAL

    private val radius: Float
    private val gapBetweenCircles: Float

    init {
        val color: Int
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.DottedLineView, 0, 0)

        try {
            gapBetweenCircles =
                attributes.getDimensionPixelSize(R.styleable.DottedLineView_dotGap, 2).toFloat()
            radius =
                attributes.getDimensionPixelSize(R.styleable.DottedLineView_dotRadius, 2).toFloat()
            color = attributes.getColor(
                R.styleable.DottedLineView_dotColor,
                context.resources.getColor(R.color.black, null)
            )
            orientation = attributes.getInt(
                R.styleable.DottedLineView_dottedLineOrientation,
                ORIENTATION_HORIZONTAL
            )
        } finally {
            attributes.recycle()
        }

        paint.color = color
    }

    override fun onDraw(canvas: Canvas) {
        val params = layoutParams

        if (orientation == ORIENTATION_HORIZONTAL) {
            params.height = (radius*2).toInt()

            path.moveTo(radius, radius)
            path.lineTo(width.toFloat(), radius)
        } else if (orientation == ORIENTATION_VERTICAL) {
            params.width = (radius*2).toInt()

            path.moveTo(radius, radius)
            path.lineTo(radius, height.toFloat())
        }
        layoutParams = params

        val circlePath = Path()
        circlePath.addCircle(0f, 0f, radius, Path.Direction.CCW)

        /**
         * Distance between the centers of two adjacent circles
         */
        val advance = radius * 2 + gapBetweenCircles
        val pathEffect: PathEffect = PathDashPathEffect(circlePath, advance, 0f, PathDashPathEffect.Style.ROTATE)
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
    }
}
