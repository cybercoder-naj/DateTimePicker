package dev.cybercoder_nishant.datetime_picker

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.time.LocalTime
import kotlin.math.abs

internal class TimeState {
    private val _scrollState = mutableStateOf(TimeScrollState())
    val scrollState: State<TimeScrollState> get() = _scrollState

    private val _time = mutableStateOf(LocalTime.now())
    val time: State<LocalTime> get() = _time

    companion object {
        private const val TAG = "TimeState"
    }

    data class TimeScrollState(
        var hours: Float = 0f,
        var minutes: Float = 0f,
        var seconds: Float = 0f,
        var component: TimeUnit? = null
    ) {

        fun offsetScroll(dy: Float): TimeScrollState =
            copy().apply {
                when (component) {
                    TimeUnit.Hours -> hours += dy
                    TimeUnit.Minutes -> minutes += dy
                    TimeUnit.Seconds -> seconds += dy
                    null -> Unit
                }
            }

        fun hasCrossedThreshold(threshold: Float): Boolean {
            return when (component) {
                TimeUnit.Hours -> abs(hours) > threshold
                TimeUnit.Minutes -> abs(minutes) > threshold
                TimeUnit.Seconds -> abs(seconds) > threshold
                null -> false
            }
        }

        fun direction(): Int {
            val value = when (component) {
                TimeUnit.Hours -> hours
                TimeUnit.Minutes -> minutes
                TimeUnit.Seconds -> seconds
                null -> 0f
            }

            return when {
                value == 0f -> 0
                value < 0f -> -1
                else -> 1
            }
        }
    }

    fun startOrResumeScrolling(unit: TimeUnit) {
        _scrollState.value = scrollState.value.copy(component = unit)
    }

    fun scrollByIfScrolling(dy: Float) {
        _scrollState.value = scrollState.value.offsetScroll(dy)
    }

    fun stopScrolling() {
        _scrollState.value = scrollState.value.copy(component = null)
    }

    fun hasCrossedThreshold(threshold: Float): Pair<TimeUnit?, Int> {
        if (!scrollState.value.hasCrossedThreshold(threshold))
            return null to 0

        return scrollState.value.component to scrollState.value.direction()
    }

    fun getTime(unit: TimeUnit): Int {
        return when (unit) {
            TimeUnit.Hours -> time.value.hour
            TimeUnit.Minutes -> time.value.minute
            TimeUnit.Seconds -> time.value.second
        }
    }
}
