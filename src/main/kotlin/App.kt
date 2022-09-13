import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Watch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.annotations.NotNull
import org.jetbrains.skia.impl.Log
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.concurrent.timerTask

const val NAME = "Clocky"

var dateTime by mutableStateOf<LocalDateTime>(LocalDateTime.of(2005, 9, 18, 0, 0))

val dateTimeList = mutableStateListOf<LocalDateTime>(LocalDateTime.of(2005, 9, 18, 0, 0))
val dateTimeZone = mutableStateListOf<ZoneId>(ZoneId.of("Asia/Seoul"))
var selectedZoneIndex by mutableStateOf<Int?>(null)

fun schedule() {
    java.util.Timer().schedule(
        timerTask {
            dateTime = LocalDateTime.now()

            dateTimeList.clear()
            dateTimeZone.forEach{ zone ->
                dateTimeList.add(LocalDateTime.now(zone))
            }
        },
        50
    )
}