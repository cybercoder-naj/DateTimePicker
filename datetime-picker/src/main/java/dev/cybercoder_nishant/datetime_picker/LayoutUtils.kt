package dev.cybercoder_nishant.datetime_picker

import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle

internal class LayoutUtils(
    val measurer: TextMeasurer,
    val style: TextStyle,
    private val layoutResult: TextLayoutResult,
    private val time: Int,
    private val unit: TimeUnit
) {
    internal val width: Float get() =
        layoutResult.size.width.toFloat()

    internal val height: Float get() =
        layoutResult.size.height.toFloat()

    internal fun getText(offset: Int = 0): String {
        val time = this.time + offset
        return formatTime(bindTime(time, unit.bound))
    }

}

private fun bindTime(time: Int, bound: Int): Int {
    var value = time
    if (value > bound)
        value %= value % bound
    while (value < 0)
        value = bound - value
    return value
}

internal fun formatTime(time: Int): String =
    String.format("%02d", time)
