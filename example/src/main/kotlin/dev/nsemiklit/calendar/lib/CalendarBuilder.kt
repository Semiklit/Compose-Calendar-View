package dev.nsemiklit.calendar.lib

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

typealias FormattedMonth = List<List<LocalDate?>>

private const val maxWeeksInMonth = 6
private const val daysInWeek = 7
fun createArrangementMonth(date: LocalDate): FormattedMonth {
    val monthInCalendar =
        List(date.lastDayOfMonth.dayOfMonth) { day ->
            date.withDayOfMonth(day.inc())
        }

    var dayPointer = 0
    val startDayOfMonth = monthInCalendar[dayPointer].dayOfWeek.isoDayNumber

    return List(maxWeeksInMonth) { week ->
        List(daysInWeek) { dayInWeek ->
            if (week == 0 && dayInWeek.inc() < startDayOfMonth) {
                null
            } else {
                monthInCalendar.getOrNull(dayPointer)
                    .also { dayPointer++ }
            }
        }
    }
}

val LocalDate.isFirstDayInWeek
    get() = dayOfWeek.isoDayNumber == 1

val LocalDate.isLastDayInWeek
    get() = dayOfWeek.isoDayNumber == daysInWeek

fun LocalDate.withDayOfMonth(dayOfMonth: Int) = LocalDate(this.year, this.month, dayOfMonth)

val LocalDate.previousMonthStart
    get() = withDayOfMonth(1).minus(1, DateTimeUnit.MONTH)

val LocalDate.nextMonthStart
    get() = withDayOfMonth(1).plus(1, DateTimeUnit.MONTH)

val LocalDate.lastDayOfMonth
    get() = nextMonthStart.minus(DatePeriod(days = 1))

val DatePeriod.totalMonths
    get() = years * 12 + months
