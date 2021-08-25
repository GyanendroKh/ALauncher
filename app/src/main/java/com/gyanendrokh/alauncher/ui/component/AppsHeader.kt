package com.gyanendrokh.alauncher.ui.component

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import com.gyanendrokh.alauncher.R

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun AppsHeader(modifier: Modifier = Modifier) {
    val context = LocalContext.current

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
                onClick = { /*TODO*/ }
            ) {
                context.getDrawable(R.drawable.ic_icons8_google_play)?.toBitmap()
                    ?.asImageBitmap()?.let {
                        Icon(
                            modifier = Modifier
                                .size(height = 28.dp, width = 28.dp),
                            bitmap = it,
                            contentDescription = "Play Store",
                            tint = Color.White
                        )
                    }
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(setting) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = { /*TODO*/ }
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
