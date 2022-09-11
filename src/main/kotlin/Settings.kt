import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window


object Settings {
    var showSettings by mutableStateOf(false)

    @Composable
    fun Settings() {
        Window(
            title = "설정",
            icon = rememberVectorPainter(Icons.Filled.Settings),
            onCloseRequest = {
                showSettings = false
            },
            visible = showSettings
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                //
            }

            Text("dsaf")
        }
    }

}