package no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import no.uio.ifi.in2000.ljvelpen.ljvelpen_oblig2.ui.theme.Ljvelpen_oblig2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ljvelpen_oblig2Theme {
                Navigate()
            }
        }
    }
}