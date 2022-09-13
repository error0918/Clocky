@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.*
import java.awt.Dimension


fun main() = application {
    Main.state = rememberWindowState()

    Window(
        icon = rememberVectorPainter(Icons.Filled.WatchLater),
        title = NAME,
        onKeyEvent = { keyEvent ->
            when (keyEvent.key) {
                Key.Zero -> {
                    Settings.showSettings = true
                }
                Key.One -> Main.index = 0
                Key.Two -> Main.index = 1
                Key.Three -> Main.index = 2
                Key.Four -> Main.index = 3
                Key.Five -> Main.index = 4

                Key.Escape -> {
                    Main.state.placement = WindowPlacement.Floating
                    Main.fullScreenMode = false
                }
                Key.F11 -> {
                    if (Main.state.placement == WindowPlacement.Fullscreen) { //todo
                        Main.state.placement = WindowPlacement.Floating
                        Main.fullScreenMode = false
                    } else {
                        Main.state.placement = WindowPlacement.Fullscreen
                        Main.fullScreenMode = true
                    }
                }
            }
            true
        }, //TODO
        onCloseRequest = ::exitApplication,
        state = Main.state,
        alwaysOnTop = Main.alwaysOnTop
    ) {
        this.window.minimumSize =
            Dimension(
                with(LocalDensity.current) { 600.dp.toPx().toInt() },
                with(LocalDensity.current) { 500.dp.toPx().toInt() }
            )

        Main.Main()

        if (Settings.showSettings) Settings.Settings()

        if(Clock.showMiniClock) Clock.MiniClock()
    }
}

object Main {
    lateinit var state: WindowState
    var alwaysOnTop by mutableStateOf(false)

    var index by mutableStateOf(0)

    private val composableList: List<@Composable (Boolean) -> Unit> = listOf({ Clock.Clock(it) }, { WorldClock.WorldClock(it) }, { Timer.Timer(it) }, { StopWatch.StopWatch(it) }, { Alarm.Alarm(it) })
    private val titles = listOf("시계", "세계 시각", "타이머", "스톱워치", "알람")
    private val icons = listOf(Icons.Filled.WatchLater, Icons.Filled.Map, Icons.Filled.HourglassFull, Icons.Filled.Timer, Icons.Filled.Alarm)

    var fullScreenMode by mutableStateOf(false)

    @Composable
    fun Main() {
        AppTheme(useDarkTheme = false) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (fullScreenMode) {
                    composableList[index](true)
                } else {
                    Row {

                        NavigationRail()

                        Scaffold(
                            topBar = { Toolbar() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp))
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(topStartPercent = 100))
                                        .background(MaterialTheme.colorScheme.background)
                                )
                            }

                            composableList[index](false)
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun NavigationRail() {
        Surface(
            tonalElevation = 10.dp
        ) {
            NavigationRail(
                header = {
                    FloatingActionButton(
                        onClick = {
                            Settings.showSettings = true
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Settings, null)
                    }
                },
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    titles.forEachIndexed { i, _ ->
                        NavigationRailItem(
                            selected = index == i,
                            onClick = {
                                index = i
                            },
                            icon = {
                                Icon(
                                    imageVector = icons[i],
                                    contentDescription = titles[i]
                                )
                            },
                            label = {
                                Text(text = titles[i])
                            }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            )
        }
    }

    @Composable
    fun Toolbar() {
        SmallTopAppBar(
            title = {
                Row {
                    Text(text = titles[index])
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = NAME, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                }
            },
            actions = { Icon(imageVector = icons[index], contentDescription = titles[index], modifier = Modifier.padding(end = 10.dp)) },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp))
        )
    }

}
