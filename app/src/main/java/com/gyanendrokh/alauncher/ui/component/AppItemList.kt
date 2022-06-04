package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gyanendrokh.alauncher.model.AppEntity

@ExperimentalFoundationApi
@Composable
fun AppItemList(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onItemClick: (AppEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(apps) { app ->
            AppItem(app = app, onPress = onItemClick)
        }
    }
}
