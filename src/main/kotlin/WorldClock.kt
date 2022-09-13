import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.ceil
import kotlin.math.floor

object WorldClock {
    var showMiniWorldClock by mutableStateOf(false)
    @Composable
    fun WorldClock(fullScreenMode: Boolean = false) {
        var contentSize by remember { mutableStateOf(IntSize.Zero) }

        for (i in 0..100) dateTimeZone.add(ZoneId.of("Asia/Seoul"))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(30.dp + 20.dp)
                .fillMaxSize()
                .onSizeChanged { size ->
                    contentSize = size
                }
        ) {
            if (contentSize != IntSize.Zero) {
                val width = with(LocalDensity.current) { contentSize.width.toDp() }
                Popup(
                    Alignment.Center
                ) {
                    Text(width.toString())
                }
                val lineCount = floor((width - 30.dp * 2 - 20.dp) / (200.dp + 20.dp)).toInt()
                val lines = ceil(lineCount.toFloat() / dateTimeZone.size.toFloat()).toInt()

                for (lineIndex in 0 until lines) {
                    for (inlineIndex in 0 until if (lineIndex == lines - 1) dateTimeZone.size - lineIndex * lineCount else lineCount) {
                        val index = 0
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            WorldClockUnit(index)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun WorldClockUnit(dateTimeIndex: Int) {
        Card(
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .size(200.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                //
            }
        }
    }

    @Composable
    fun MiniWorldClock(dateTime: LocalDateTime) {
        val state = rememberWindowState(
            position = WindowPosition(Alignment.BottomEnd),
            size = DpSize(300.dp, 300.dp)
        )
        Window(
            title = "미니 세계 시각",
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
                                        Main.index = 0
                                        Main.state.isMinimized = false
                                        Main.alwaysOnTop = true
                                        Main.alwaysOnTop = false
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PictureInPicture,
                                        contentDescription = "원래 화면으로 돌아가기"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        showMiniWorldClock = false
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
                                //TODO
                            }

                        }
                    }
                }
            }
        }
    }

}