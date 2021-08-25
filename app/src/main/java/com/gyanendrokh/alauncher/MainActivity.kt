package com.gyanendrokh.alauncher

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.accompanist.pager.ExperimentalPagerApi
import com.gyanendrokh.alauncher.ui.component.Pager
import com.gyanendrokh.alauncher.ui.theme.LauncherTheme
import com.gyanendrokh.alauncher.util.isDefaultLauncher
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    private val appsViewModel: AppsViewModel by viewModels()

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
    }

    override fun onResume() {
        super.onResume()

        // TODO: Find something better.
        Handler(Looper.myLooper()!!).post {
            appsViewModel.updateApps()
            appsViewModel.updateFeaturedApps()
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
