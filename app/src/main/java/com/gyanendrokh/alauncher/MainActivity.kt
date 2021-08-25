package com.gyanendrokh.alauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.gyanendrokh.alauncher.ui.component.Pager
import com.gyanendrokh.alauncher.ui.theme.LauncherTheme
import com.gyanendrokh.alauncher.util.queryAllPackages

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LauncherTheme {
                Pager()
            }
        }

        queryAllPackages(this).forEach { app ->
            println("App ${app.label} -> ${app.packageName}")
        }
    }
}
