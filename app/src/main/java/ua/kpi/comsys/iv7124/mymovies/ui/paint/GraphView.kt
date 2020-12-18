package ua.kpi.comsys.iv7124.mymovies.ui.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

class GraphView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private val p = Paint()
    private val bound = 2 * PI
    private val step = 0.1f

    private lateinit var c: Canvas
    private var wOffset: Int = 0
    private var hOffset: Int = 0
    private var axisLength = 0f
    private var one = 0f

    override fun onDraw(canvas: Canvas) {
        c = canvas
        wOffset = canvas.width / 2
        hOffset = canvas.height / 2

        drawAxis()
        drawGraph()
    }

    private fun drawAxis() {
        p.color = Color.BLACK
        p.strokeWidth = 10f

        fun drawLine(startX: Float, startY: Float, endX: Float, endY: Float) {
            c.drawLine(xOffset(startX), yOffset(startY), xOffset(endX), yOffset(endY), p)
        }

        fun drawSymLine(startX: Float, startY: Float, endX: Float, endY: Float) {
            drawLine(startX, startY, endX, endY)
            drawLine(startY, -startX, endY, -endX)
        }

        axisLength = (if (wOffset < hOffset) wOffset -10 else hOffset -10).toFloat()
        one = (axisLength / bound).roundToInt().toFloat()
        val arrowLength = 30

        drawSymLine(-axisLength, 0f, axisLength, 0f)

        drawSymLine(axisLength-3, 0f, axisLength - arrowLength -3, 0f + arrowLength)
        drawSymLine(axisLength-3, 0f, axisLength - arrowLength -3, 0f - arrowLength)

        drawSymLine(one, -15f, one, 15f)
    }

    private fun drawGraph() {
        p.color = Color.BLUE
        p.strokeWidth = 7f

        val points = mutableListOf<Float>()
        var x = - axisLength / one
        points.add(xOffset((x * one)))
        points.add(yOffset((-sin(x) * one)))
        x += step
        while (x < axisLength / one) {
            val y = sin(x)
            points.add(xOffset((x * one)))
            points.add(yOffset((-y * one)))
            points.add(xOffset((x * one)))
            points.add(yOffset((-y * one)))
            x += step
        }
        c.drawLines(points.toFloatArray(), p)
    }

    private fun xOffset(x: Float): Float {return x + wOffset}
    private fun yOffset(y: Float): Float {return y + hOffset}
}