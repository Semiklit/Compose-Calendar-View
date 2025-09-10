package dev.nsemiklit.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.nsemiklit.calendar.lib.MonthView
import dev.nsemiklit.calendar.theme.CalendarPreviewTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun App() {
    CalendarPreviewTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            MonthView(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                today = today,
                startDate = today.minus(3, DateTimeUnit.DAY),
                endDate = today,
                maxDate = today.plus(1, DateTimeUnit.DAY),
            ) { _ -> }
        }
    }
}