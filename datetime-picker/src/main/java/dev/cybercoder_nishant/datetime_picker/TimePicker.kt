package dev.cybercoder_nishant.datetime_picker

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEvents

@Composable
fun TimePicker(
    modifier: Modifier = Modifier
) {
    val dividerLength = 128.dp.value
    val deviation = 2

    val state by remember { mutableStateOf(TimeState()) }

    val hoursLayout = getLayoutResult(state, TimeUnit.Hours)
    val minsLayout = getLayoutResult(state, TimeUnit.Minutes)
    val secsLayout = getLayoutResult(state, TimeUnit.Seconds)
    val threshold = hoursLayout.height / 2f

    var canvasSize by remember { mutableStateOf(Size.Unspecified) }
    var oneThird by remember { mutableFloatStateOf(0f) }
    var hoursPos by remember { mutableStateOf(Offset.Zero) }
    var minsPos by remember { mutableStateOf(Offset.Zero) }
    var secsPos by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(key1 = canvasSize) {
        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

        oneThird = canvasSize.width / 3f
        hoursPos = Offset(oneThird / 2f, center.y)
        minsPos = Offset(center.x, center.y)
        secsPos = Offset((oneThird * 2f + canvasSize.width) / 2, center.y)
    }

    LaunchedEffect(key1 = state) {
        Log.d("TimePicker", "LaunchedEffect called with state=$state")
        val (candidate, offset) = state.hasCrossedThreshold(threshold)

//        when (candidate) {
//            TimeUtils.TimeUnit.Hours -> currentTime.hours += offset
//            TimeUtils.TimeUnit.Minutes -> currentTime.minutes += offset
//            TimeUtils.TimeUnit.Seconds -> currentTime.seconds += offset
//            null -> Unit
//        }
    }

    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerMotionEvents(
                onDown = {
                    motionEvent = MotionEvent.Down
                    when (it.position.x) {
//                        in 0f..<oneThird -> state.componentInScroll = TimeUtils.TimeUnit.Hours
//                        in oneThird..<(2 * oneThird) -> state.componentInScroll =
//                            TimeUtils.TimeUnit.Minutes
//
//                        else -> state.componentInScroll = TimeUtils.TimeUnit.Seconds
                    }

                    previousPosition = it.position
                    it.consume()
                },
                onMove = {
                    motionEvent = MotionEvent.Move
                    state.scrollBy(it.position.y - previousPosition.y)

                    previousPosition = it.position
                    it.consume()
                },
                onUp = {
                    motionEvent = MotionEvent.Up

                    previousPosition = Offset.Unspecified
                    it.consume()
                }
            )
    ) {
        canvasSize = size

        for (i in -deviation..deviation) {
            val hoursOffset = hoursPos - Offset(
                x = hoursLayout.width / 2f,
                y = hoursLayout.height / 2f - i * hoursLayout.height - state.scrollState.value.hours
            )
            if (hoursOffset.y > size.height)
                continue
            drawText(
                textMeasurer = hoursLayout.measurer,
                text = hoursLayout.getText(offset = i),
                topLeft = hoursOffset,
                style = hoursLayout.style
            )
        }

        for (i in -deviation..deviation) {
            val minsOffset = minsPos - Offset(
                x = minsLayout.width / 2f,
                y = minsLayout.height / 2f - i * minsLayout.height - state.scrollState.value.minutes
            )
            if (minsOffset.y > size.height)
                continue
            drawText(
                textMeasurer = minsLayout.measurer,
                text = minsLayout.getText(offset = i),
                topLeft = minsOffset,
                style = minsLayout.style
            )
        }

        for (i in -deviation..deviation) {
            val secsOffset = secsPos - Offset(
                x = secsLayout.width / 2f,
                y = secsLayout.height / 2f - i * secsLayout.height - state.scrollState.value.seconds
            )
            if (secsOffset.y > size.height)
                continue
            drawText(
                textMeasurer = secsLayout.measurer,
                text = secsLayout.getText(offset = i),
                topLeft = secsOffset,
                style = secsLayout.style
            )
        }

        drawRect(
            color = Color.Gray,
            topLeft = Offset(0f, center.y - hoursLayout.height * 3 / 2),
            size = size.copy(height = hoursLayout.height),
            blendMode = BlendMode.Screen
        )
        drawRect(
            color = Color.Gray,
            topLeft = Offset(0f, center.y + hoursLayout.height / 2),
            size = size.copy(height = hoursLayout.height),
            blendMode = BlendMode.Screen
        )

        drawLine(
            color = Color.Gray,
            start = Offset(oneThird, center.y - dividerLength / 2),
            end = Offset(oneThird, center.y + dividerLength / 2),
            strokeWidth = 4.dp.value
        )

        drawLine(
            color = Color.Gray,
            start = Offset(2 * oneThird, center.y - dividerLength / 2),
            end = Offset(2 * oneThird, center.y + dividerLength / 2),
            strokeWidth = 4.dp.value
        )

    }
}

@Composable
private fun getLayoutResult(state: TimeState, unit: TimeUnit): LayoutUtils {
    val fontSize = 36.sp

    val time = state.getTime(unit)
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


@Preview(device = "id:pixel_6", showBackground = true, showSystemUi = true)
@Composable
fun TimePickerPreview() {
    TimePicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 42.dp)
            .border(1.dp, Color.Black)
    )
}