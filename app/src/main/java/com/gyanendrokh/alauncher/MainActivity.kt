package com.gyanendrokh.alauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import com.gyanendrokh.alauncher.navigation.Navigation
import com.gyanendrokh.alauncher.theme.LauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BackHandler {
                // Note: To prevent app from closing
            }
            LauncherTheme {
                Navigation()
            }
        }
    }
}
