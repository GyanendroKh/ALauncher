package com.gyanendrokh.alauncher.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.gyanendrokh.alauncher.R

@Composable
fun AppsHeader(
    modifier: Modifier = Modifier,
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Apps",
                fontSize = 35.sp
            )
        }

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (store, setting) = createRefs()

            IconButton(
                modifier = Modifier
                    .constrainAs(store) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(setting.start)
                    },
                onClick = onStoreClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(height = 28.dp, width = 28.dp),
                    painter = painterResource(id = R.drawable.ic_icons8_google_play),
                    contentDescription = "Play Store",
                    tint = Color.White
                )
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(setting) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = onSettingsClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(height = 28.dp, width = 28.dp),
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.7.dp,
            color = Color.White
        )
    }
}
