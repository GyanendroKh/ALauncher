package com.gyanendrokh.alauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.gyanendrokh.alauncher.ui.component.Pager
import com.gyanendrokh.alauncher.ui.theme.LauncherTheme
import com.gyanendrokh.alauncher.util.isDefaultLauncher
import com.gyanendrokh.alauncher.util.queryAllPackages

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    companion object {
        private var onBackPressedCallBack: () -> Boolean = { false }

        fun setOnBackPressCallback(callback: () -> Boolean) {
            onBackPressedCallBack = callback
        }
    }

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

    override fun onBackPressed() {
        if (!onBackPressedCallBack()) {
            if (!isDefaultLauncher(this)) {
                super.onBackPressed()
            }

            // Prevent closing the app if it is the default launcher.
        }
    }
}
