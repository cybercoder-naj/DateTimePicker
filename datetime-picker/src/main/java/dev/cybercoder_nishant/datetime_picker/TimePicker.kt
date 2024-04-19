package dev.cybercoder_nishant.datetime_picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimePicker(
    modifier: Modifier = Modifier
) {
    val dividerLength = 128.dp.value
    val deviation = 1

    val timeNow = TimeUtils.now()
    var currentTime by remember {
        mutableStateOf(timeNow)
    }

    val hoursLayout = currentTime.getLayoutResult(unit = TimeUtils.TimeUnit.Hours)
    val minsLayout = currentTime.getLayoutResult(unit = TimeUtils.TimeUnit.Minutes)
    val secsLayout = currentTime.getLayoutResult(unit = TimeUtils.TimeUnit.Seconds)

    Canvas(
        modifier = modifier
            .clipToBounds()
    ) {
        val oneThird = size.width / 3f
        val hoursPos = oneThird / 2f
        val minsPos = center.x
        val secsPos = (2 * oneThird + size.width) / 2

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = hoursLayout.measurer,
                text = hoursLayout.getText(offset = i),
                topLeft = Offset(
                    x = hoursPos - hoursLayout.width / 2f,
                    y = center.y - (hoursLayout.height / 2f - i * hoursLayout.height)
                ),
                style = hoursLayout.style
            )
        }

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = minsLayout.measurer,
                text = minsLayout.getText(offset = i),
                topLeft = Offset(
                    x = minsPos - minsLayout.width / 2f,
                    y = center.y - (minsLayout.height / 2f - i * minsLayout.height)
                ),
                style = minsLayout.style
            )
        }

        for (i in -deviation..deviation) {
            drawText(
                textMeasurer = secsLayout.measurer,
                text = secsLayout.getText(offset = i),
                topLeft = Offset(
                    x = secsPos - secsLayout.width / 2f,
                    y = center.y - (secsLayout.height / 2f - i * secsLayout.height)
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