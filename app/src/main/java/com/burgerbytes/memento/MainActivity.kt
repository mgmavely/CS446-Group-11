package com.burgerbytes.memento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.burgerbytes.memento.theme.MementoTheme
import org.burgerbytes.MementoApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MementoTheme(darkTheme = false) {
                MementoApp()
            }
        }
    }
}
