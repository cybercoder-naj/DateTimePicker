package dev.cybercoder_nishant.datetime_picker

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
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEvents

@Composable
fun TimePicker(
    modifier: Modifier = Modifier
) {
    val dividerLength = 128.dp.value
    val deviation = 1

    val utils by remember { mutableStateOf(TimeUtils.now()) }
    var state by remember { mutableStateOf(TimeState()) }

    val hoursLayout = utils.getLayoutResult(unit = TimeUtils.TimeUnit.Hours)
    val minsLayout = utils.getLayoutResult(unit = TimeUtils.TimeUnit.Minutes)
    val secsLayout = utils.getLayoutResult(unit = TimeUtils.TimeUnit.Seconds)

    var canvasSize by remember { mutableStateOf(Size.Unspecified) }
    var oneThird by remember { mutableFloatStateOf(0f) }
    var hoursPos by remember { mutableStateOf(Offset.Zero) }
    var minsPos by remember { mutableStateOf(Offset.Zero) }
    var secsPos by remember { mutableStateOf(Offset.Zero) }

    var scrollDy by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(key1 = canvasSize) {
        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

        oneThird = canvasSize.width / 3f
        hoursPos = Offset(oneThird / 2f, center.y)
        minsPos = Offset(center.x, center.y)
        secsPos = Offset((oneThird * 2f + canvasSize.width) / 2, center.y)
    }

    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var lockedPosition by remember { mutableStateOf(Offset.Unspecified) }
    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerMotionEvents(
                onDown = {
                    motionEvent = MotionEvent.Down
                    lockedPosition = it.position

                    when (lockedPosition.x) {
                        in 0f..<oneThird -> state.componentInScroll = TimeUtils.TimeUnit.Hours
                        in oneThird..<(2 * oneThird) -> state.componentInScroll = TimeUtils.TimeUnit.Minutes
                        else -> state.componentInScroll = TimeUtils.TimeUnit.Seconds
                    }

                    it.consume()
                },
                onMove = {
                    motionEvent = MotionEvent.Move
                    scrollDy = it.position.y - lockedPosition.y
                    it.consume()
                },
                onUp = {
                    motionEvent = MotionEvent.Up
                    lockedPosition = Offset.Unspecified
                    it.consume()
                }
            )
    ) {
        canvasSize = size

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = hoursLayout.measurer,
                text = hoursLayout.getText(offset = i),
                topLeft = hoursPos - Offset(
                    x = hoursLayout.width / 2f,
                    y = hoursLayout.height / 2f - i * hoursLayout.height - scrollDy
                ),
                style = hoursLayout.style
            )
        }

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = minsLayout.measurer,
                text = minsLayout.getText(offset = i),
                topLeft = minsPos - Offset(
                    x = minsLayout.width / 2f,
                    y = minsLayout.height / 2f - i * minsLayout.height - scrollDy
                ),
                style = minsLayout.style
            )
        }

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = secsLayout.measurer,
                text = secsLayout.getText(offset = i),
                topLeft = secsPos - Offset(
                    x = secsLayout.width / 2f,
                    y = secsLayout.height / 2f - i * secsLayout.height - scrollDy
                ),
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