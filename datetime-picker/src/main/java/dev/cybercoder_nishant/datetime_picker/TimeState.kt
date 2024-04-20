package dev.cybercoder_nishant.datetime_picker

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlin.math.abs

internal data class TimeState(
    var componentInScroll: TimeUtils.TimeUnit? = null
) {
    private val _scrollState = mutableStateOf(TimeScrollState())
    val scrollState: State<TimeScrollState> get() = _scrollState

    companion object {
        private const val TAG = "TimeState"
    }

    @Suppress("LocalVariableName")
    class TimeScrollState(
        _hours: Float = 0f,
        _minutes: Float = 0f,
        _seconds: Float = 0f
    ) {
        var hours: Float
            private set
        var minutes: Float
            private set
        var seconds: Float
            private set

        init {
            hours = _hours
            minutes = _minutes
            seconds = _seconds
        }

        fun offsetScroll(dy: Float, unit: TimeUtils.TimeUnit): TimeScrollState =
            copy().apply {
                when (unit) {
                    TimeUtils.TimeUnit.Hours -> hours += dy
                    TimeUtils.TimeUnit.Minutes -> minutes += dy
                    TimeUtils.TimeUnit.Seconds -> seconds += dy
                }
            }

        fun copy(): TimeScrollState {
            return TimeScrollState(hours, minutes, seconds)
        }
    }

    fun scrollBy(dy: Float) {
        if (componentInScroll == null) {
            Log.e(TAG, "scrollBy: componentInScroll is null")
            return
        }

        _scrollState.value = scrollState.value.offsetScroll(dy, componentInScroll!!)
    }

    fun hasCrossedThreshold(threshold: Float): Pair<TimeUtils.TimeUnit?, Int> {
        val checkCrossed = { scrollDy: Float ->
            val crossed = abs(scrollDy) > threshold
            if (!crossed)
                null to 0
            else if (scrollDy < 0)
                componentInScroll to -1
            else
                componentInScroll to 1
        }

        return when (componentInScroll) {
            TimeUtils.TimeUnit.Hours -> checkCrossed(scrollState.value.hours)
            TimeUtils.TimeUnit.Minutes -> checkCrossed(scrollState.value.minutes)
            TimeUtils.TimeUnit.Seconds -> checkCrossed(scrollState.value.seconds)
            null -> null to 0
        }
    }
}
