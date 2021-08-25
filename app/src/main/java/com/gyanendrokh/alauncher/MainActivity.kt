package com.gyanendrokh.alauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gyanendrokh.alauncher.ui.component.Greeting
import com.gyanendrokh.alauncher.ui.theme.LauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LauncherTheme {
                Greeting("Android")
            }
        }
    }
}
