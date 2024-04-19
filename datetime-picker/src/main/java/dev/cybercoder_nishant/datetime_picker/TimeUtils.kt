package dev.cybercoder_nishant.datetime_picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import java.time.LocalTime

data class TimeUtils(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {
    internal enum class TimeUnit {
        Hours, Minutes, Seconds;

        val bound: Int get() =
            when(this) {
                Hours -> 24
                Minutes -> 60
                Seconds -> 60
            }
    }

    companion object {
        internal fun now(): TimeUtils {
            val time = LocalTime.now()
            return TimeUtils(
                hours = time.hour,
                minutes = time.minute,
                seconds = time.second
            )
        }
    }

    internal inner class LayoutUtils(
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

        private fun bindTime(time: Int, bound: Int): Int {
            var value = time
            if (value > bound)
                value %= value % bound
            while (value < 0)
                value = bound - value
            return value
        }
    }

    @Composable
    internal fun getLayoutResult(unit: TimeUnit): LayoutUtils {
        val fontSize = 36.sp

        val time = getTime(unit)
        val measurer = rememberTextMeasurer()
        val style = TextStyle(
            fontSize = fontSize,
            color = Color.Black
        )
        val layoutResult = remember(time) {
            measurer.measure(formatTime(time), style)
        }
        return LayoutUtils(measurer, style, layoutResult, time, unit)
    }

    private fun formatTime(time: Int): String =
        String.format("%02d", time)

    private fun getTime(unit: TimeUnit): Int =
        when (unit) {
            TimeUnit.Hours -> hours
            TimeUnit.Minutes -> minutes
            TimeUnit.Seconds -> seconds
        }
}