package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.gyanendrokh.alauncher.model.AppEntity

@Composable
fun AppItemList(
    apps: List<AppEntity>,
    onItemClick: (AppEntity) -> Unit
) {
    LazyColumn {
        items(apps) { app ->
            AppItem(app = app, onClick = onItemClick)
        }
    }
}
