package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.util.openApp

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    apps: List<AppEntity>,
    onAppDrawerClick: () -> Unit = {}
) {
    val ctx = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (apps.isNotEmpty()) {
            apps.subList(0, 2).forEach {
                Image(
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp)
                        .clickable {
                            openApp(ctx, it.packageName)
                        },
                    bitmap = it.icon.toBitmap().asImageBitmap(),
                    contentDescription = it.label
                )
            }
        }

        IconButton(
            modifier = Modifier
                .size(
                    height = 45.dp,
                    width = 45.dp
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(50)
                )
                .padding(5.dp),
            onClick = onAppDrawerClick
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Outlined.List,
                contentDescription = "App Drawer",
                tint = Color.White
            )
        }

        if (apps.isNotEmpty()) {
            apps.subList(2, apps.size).forEach {
                Image(
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp)
                        .clickable {
                            openApp(ctx, it.packageName)
                        },
                    bitmap = it.icon.toBitmap().asImageBitmap(),
                    contentDescription = it.label
                )
            }
        }
    }
}
