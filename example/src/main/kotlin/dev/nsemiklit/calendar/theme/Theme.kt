package dev.nsemiklit.calendar.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CalendarPreviewColors(
    val primaryBackground: Color,
    val accentColor: Color,
    val primaryText: Color,
    val secondaryText: Color,
)

object CalendarPreviewTheme {
    internal val colors: CalendarPreviewColors
        @Composable
        get() = LocalCalendarPreviewColors.current
}

internal val LocalCalendarPreviewColors = staticCompositionLocalOf<CalendarPreviewColors> {
    error("No colors provided")
}



@Composable
fun CalendarPreviewTheme(
    colors: CalendarPreviewColors = LightPalette,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalCalendarPreviewColors provides colors,
        content = content
    )
}