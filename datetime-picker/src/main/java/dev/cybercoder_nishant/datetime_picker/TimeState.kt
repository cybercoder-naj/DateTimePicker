package dev.cybercoder_nishant.datetime_picker

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

internal data class TimeState(
    var componentInScroll: TimeUtils.TimeUnit? = null
) {
    private val _scrollDy = mutableStateOf(TimeScrollState())
    val scrollDy: State<TimeScrollState> get() = _scrollDy

    companion object {
        private const val TAG = "TimeState"
    }

    data class TimeScrollState(
        var hours: Float = 0f,
        var minutes: Float = 0f,
        var seconds: Float = 0f
    ) {
        fun offsetScroll(dy: Float, unit: TimeUtils.TimeUnit): TimeScrollState {
            val clone = this.copy()
            when (unit) {
                TimeUtils.TimeUnit.Hours -> clone.hours += dy
                TimeUtils.TimeUnit.Minutes -> clone.minutes += dy
                TimeUtils.TimeUnit.Seconds -> clone.seconds += dy
            }
            return clone
        }
    }

    fun scrollBy(dy: Float) {
//        Log.d(TAG, "scrollBy() called with: dy = $dy")
        componentInScroll?.let {
            _scrollDy.value = scrollDy.value.offsetScroll(dy, it)
        } ?: Log.e(TAG, "scrollBy: componentInScroll is null")

        Log.d(TAG, "scrollBy: scrollDy = $scrollDy")
    }

//    fun setScroll(scroll: Float) {
//        when (componentInScroll) {
//            TimeUtils.TimeUnit.Hours -> scrollDy.hours = scroll
//            TimeUtils.TimeUnit.Minutes -> scrollDy.minutes = scroll
//            TimeUtils.TimeUnit.Seconds -> scrollDy.seconds = scroll
//            null -> TODO()
//        }
//    }
}
