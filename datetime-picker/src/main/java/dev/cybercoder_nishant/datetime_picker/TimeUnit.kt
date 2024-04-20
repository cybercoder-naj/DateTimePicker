package dev.cybercoder_nishant.datetime_picker

internal enum class TimeUnit {
    Hours, Minutes, Seconds;

    val bound: Int get() =
        when(this) {
            Hours -> 24
            Minutes -> 60
            Seconds -> 60
        }
}