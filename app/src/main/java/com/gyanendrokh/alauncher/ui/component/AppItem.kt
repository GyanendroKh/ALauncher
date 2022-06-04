package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.gyanendrokh.alauncher.model.AppEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
    modifier: Modifier = Modifier,
    app: AppEntity,
    onPress: (AppEntity) -> Unit = {},
    onLongPress: (AppEntity) -> Unit = {}
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = { onPress(app) },
                onLongClick = { onLongPress(app) }
            )
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = app.label,
            fontSize = 20.sp
        )

        Image(
            bitmap = app.icon.toBitmap().asImageBitmap(),
            contentDescription = app.label,
            modifier = Modifier.size(width = 45.dp, height = 45.dp)
        )
    }
}
