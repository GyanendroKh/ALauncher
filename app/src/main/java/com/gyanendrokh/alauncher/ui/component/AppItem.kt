package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.gyanendrokh.alauncher.model.AppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
    modifier: Modifier = Modifier,
    app: AppEntity,
    onPress: (AppEntity) -> Unit = {},
    onLongPress: (AppEntity) -> Unit = {}
) {
    var bitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(app) {
        withContext(Dispatchers.Default) {
            bitmap = app.icon.toBitmap().asImageBitmap()
        }
    }

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
            fontSize = 20.sp,
            color = Color.White
        )

        bitmap.let { bitmap ->
            if (bitmap == null) {
                Spacer(
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                )
            } else {
                Image(
                    bitmap = bitmap,
                    contentDescription = app.label,
                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                )
            }
        }
    }
}
