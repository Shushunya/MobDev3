package ua.kpi.comsys.iv7124.mymovies.ui.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class DiagramView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private val p = Paint()
    private var r1 = 0f
    private var r2 = 0f
    private val oval = RectF()

    private var wOffset: Int = 0
    private var hOffset: Int = 0

    override fun onDraw(canvas: Canvas) {
        wOffset = canvas.width / 2
        hOffset = canvas.height / 2
        p.style = Paint.Style.FILL

        r2 = (if (wOffset < hOffset) wOffset * 0.8 else hOffset * 0.8).toFloat()
        r1 = (r2 * 0.7).toFloat()

        oval.set(xOffset(-r2), yOffset(-r2), xOffset(r2), yOffset(r2))

        val angles = listOf(0.25, 0.20, 0.55).map { (360 * it).toFloat() }

        p.color = Color.YELLOW
        canvas.drawArc(oval, 0f, angles[0], true, p)
        p.color = Color.GREEN
        canvas.drawArc(oval, angles[0], angles[1], true, p)
        p.color = Color.BLUE
        canvas.drawArc(oval, angles[0] + angles[1], angles[2], true, p)

        p.color = Color.WHITE
        oval.set(xOffset(-r1), yOffset(-r1), xOffset(r1), yOffset(r1))
        canvas.drawArc(oval, 0f, 360f, true, p)
    }

    private fun xOffset(x: Float): Float {return x + wOffset}
    private fun yOffset(y: Float): Float {return y + hOffset}
}