import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeDialog
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import java.awt.Dimension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.swing.JPanel
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask
import kotlin.reflect.KProperty

object Clock {
    var showDate by mutableStateOf(false)
    var showMillisecond by mutableStateOf(false)

    @Composable
    fun Clock(fullScreenMode: Boolean = false) {
        var showDropDownMenu by remember { mutableStateOf(false) }

        schedule()

        // ----------------------------------------------------------- //

        test()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (fullScreenMode) {
                ExtendedFloatingActionButton(
                    onClick = {
                        Main.state.placement = WindowPlacement.Floating
                        Main.fullScreenMode = false
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    text = {
                        Text(
                            text = "닫기 (ESC)",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    modifier = Modifier.align(Alignment.TopStart).padding(20.dp)
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(IntrinsicSize.Min)
                ) {


                    if (!Main.fullScreenMode) {
                        IconButton(
                            onClick = {
                                Main.state.placement = WindowPlacement.Fullscreen
                                Main.fullScreenMode = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Fullscreen,
                                contentDescription = null
                            )
                        }
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PictureInPicture,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            showDropDownMenu = !showDropDownMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )

                        DropdownMenu(
                            expanded = showDropDownMenu,
                            onDismissRequest = { showDropDownMenu = false }
                        ) {
                            val items = listOf(
                                "날짜보기" to ::showDate,
                                "밀리초 보기" to ::showMillisecond
                            )

                            items.forEach { item ->
                                val value = item.second.getter()
                                DropdownMenuItem(
                                    onClick = {
                                        item.second.set(!value)
                                    }
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = item.first,
                                            Modifier.align(Alignment.CenterStart).padding(end = 48.dp + 10.dp)
                                        )
                                        Switch(
                                            checked = value,
                                            onCheckedChange = { item.second.set(!value) },
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                                uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                                uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (showDate) {
                    Text(
                        text = dateTime.format(
                            DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd (${
                                    dateTime.dayOfWeek.getDisplayName(
                                        TextStyle.FULL,
                                        Locale.getDefault()
                                    )
                                })"
                            )
                        ),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                Row {
                    Text(
                        text = dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 2)
                    )
                    if (showMillisecond) {
                        Text(
                            text = ".${dateTime.format(DateTimeFormatter.ofPattern("SSS"))}",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier.align(Alignment.Bottom)
                        )
                    }
                }


            }
        }
    }

    /////////////////////////////////////////////////////////////////
    @Composable
    fun test() {
        val state = rememberWindowState(
            position = WindowPosition(Alignment.BottomEnd),
            size = DpSize(300.dp, 300.dp)
        )
        Window(
            title = "saf",
            icon = rememberVectorPainter(Icons.Filled.LockClock),
            undecorated = true,
            onCloseRequest = {
            },
            transparent = true,
            resizable = false,
            alwaysOnTop = true,
            state = state
        ) {
            AppTheme(
                useDarkTheme = false
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp).copy(alpha = 0.9f),
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    WindowDraggableArea(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(20.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        //
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PictureInPicture,
                                        contentDescription = "원래 화면으로 돌아가기"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        //
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "닫기"
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                if (showDate) {
                                    Text(
                                        text = dateTime.format(
                                            DateTimeFormatter.ofPattern(
                                                "yyyy-MM-dd (${
                                                    dateTime.dayOfWeek.getDisplayName(
                                                        TextStyle.FULL,
                                                        Locale.getDefault()
                                                    )
                                                })"
                                            )
                                        ),
                                        style = MaterialTheme.typography.displaySmall.copy(fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.5)
                                    )
                                }

                                Row {
                                    Text(
                                        text = dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")),
                                        style = MaterialTheme.typography.displayLarge
                                    )
                                    if (showMillisecond) {
                                        Text(
                                            text = ".${dateTime.format(DateTimeFormatter.ofPattern("SSS"))}",
                                            style = MaterialTheme.typography.displaySmall.copy(fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.5),
                                            modifier = Modifier.align(Alignment.Bottom)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////

}