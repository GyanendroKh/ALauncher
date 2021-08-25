package com.gyanendrokh.alauncher.ui.component.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gyanendrokh.alauncher.model.AppEntity

@Composable
fun AppsPage(modifier: Modifier = Modifier, apps: List<AppEntity>) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(apps) { app ->
                Text(text = app.label)
            }
        }
    }
}
