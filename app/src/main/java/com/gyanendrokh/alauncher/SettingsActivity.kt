package com.gyanendrokh.alauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.gyanendrokh.alauncher.ui.theme.LauncherTheme
import com.gyanendrokh.alauncher.viewmodel.AppsViewModel

class SettingsActivity : ComponentActivity() {
    private val appsViewModel: AppsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LauncherTheme {
                LazyColumn {
                    val hiddenApps = appsViewModel.hiddenApps.value

                    items(appsViewModel.apps.value) { app ->
                        val isChecked = hiddenApps.contains(app.packageName)

                        Row(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        if (isChecked) {
                                            appsViewModel.removeHidden(listOf(app.packageName))
                                        } else {
                                            appsViewModel.addHidden(listOf(app.packageName))
                                        }
                                    }
                                )
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    bitmap = app.icon.toBitmap().asImageBitmap(),
                                    contentDescription = app.label,
                                    modifier = Modifier.size(width = 45.dp, height = 45.dp)
                                )

                                Box(modifier = Modifier.width(20.dp))

                                Text(
                                    text = app.label,
                                    fontSize = 20.sp
                                )
                            }

                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    if (isChecked) {
                                        appsViewModel.removeHidden(listOf(app.packageName))
                                    } else {
                                        appsViewModel.addHidden(listOf(app.packageName))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}
