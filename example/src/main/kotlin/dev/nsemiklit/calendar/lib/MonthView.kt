package dev.nsemiklit.calendar.lib

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.nsemiklit.calendar.theme.CalendarPreviewTheme
import dev.nsemiklit.calendar.utils.squareSize
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
fun MonthView(
    modifier: Modifier,
    today: LocalDate,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(modifier) {
        Row {
            listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС").forEach { dayOfWeek ->
                Box(Modifier.weight(1f)) {
                    BasicText(
                        modifier = Modifier.align(Alignment.Center),
                        text = dayOfWeek,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = CalendarPreviewTheme.colors.primaryText
                        ),

                        )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        createArrangementMonth(today).forEach { week ->
            Row(
                Modifier.heightIn(max = 36.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { dayInWeek ->
                    if (dayInWeek != null) {
                        Box(Modifier.weight(1f)) {
                            Box(
                                Modifier
                                    .padding(vertical = 1.dp)
                                    .selectPeriod(
                                        dayInWeek,
                                        startDate,
                                        endDate,
                                        CalendarPreviewTheme.colors.secondaryText.copy(alpha = 0.2f)
                                    )
                                    .fillMaxSize()
                            )
                            if ((minDate == null || dayInWeek >= minDate) && dayInWeek <= maxDate) {
                                Box(
                                    Modifier
                                        .align(Alignment.Center)
                                        .clip(CircleShape)
                                        .selectedDate(
                                            dayInWeek,
                                            startDate,
                                            endDate,
                                            CalendarPreviewTheme.colors.accentColor
                                        )
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
//                                            indication = ripple(),
                                            indication = null,
                                            onClick = { onDateSelected.invoke(dayInWeek) }
                                        )
                                        .squareSize()
                                        .fillMaxSize()
                                )
                            }

                            BasicText(
                                modifier = Modifier.align(Alignment.Center),
                                text = dayInWeek.dayOfMonth.toString(),
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp,
                                    color = when {
                                        dayInWeek == startDate || dayInWeek == endDate ->
                                            CalendarPreviewTheme.colors.primaryBackground


                                        minDate != null && dayInWeek < minDate ->
                                            CalendarPreviewTheme.colors.secondaryText.copy(alpha = 0.4f)

                                        dayInWeek > maxDate ->
                                            CalendarPreviewTheme.colors.secondaryText.copy(alpha = 0.4f)

                                        else -> CalendarPreviewTheme.colors.secondaryText
                                    }
                                ),
                            )
                        }

                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Stable
private fun Modifier.selectedDate(
    day: LocalDate,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    color: Color,
): Modifier = if (day == startDate || day == endDate) {
    background(color)
} else {
    this
}

@Stable
private fun Modifier.selectPeriod(
    day: LocalDate,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    color: Color,
): Modifier =
    if (startDate != null && endDate != null && day in startDate..endDate) {
        when {
            day.isLastDayInWeek || day == day.lastDayOfMonth ->
                clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))

            day.isFirstDayInWeek || day.dayOfMonth == 1 ->
                clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))

            else -> this
        }
            .drawWithContent {
                when (day) {
                    endDate -> clipRect(right = size.width / 2f) {
                        this@drawWithContent.drawContent()
                    }

                    startDate -> clipRect(left = size.width / 2f) {
                        this@drawWithContent.drawContent()
                    }

                    else -> this@drawWithContent.drawContent()
                }

            }
            .background(color)
    } else {
        this
    }

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    val today = LocalDate(2024, 6, 24)
    CalendarPreviewTheme {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MonthView(Modifier, today.previousMonthStart, maxDate = today) {}
            MonthView(
                Modifier,
                today,
                today.withDayOfMonth(6),
                today.withDayOfMonth(21),
                maxDate = today
            ) {}
            MonthView(
                Modifier,
                today.nextMonthStart,
                maxDate = today.plus(1, DateTimeUnit.MONTH)
            ) {}
        }
    }
}
