package com.gyanendrokh.alauncher.util

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.model.DateTimeEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun createQueryIntent(): Intent {
    return Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }
}

fun getAppIcon(context: Context, packageName: String): Drawable? {
    var drawable: Drawable? = null

    try {
        // try getting the properly colored launcher icons
        val launcher = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val activityList =
            launcher.getActivityList(packageName, android.os.Process.myUserHandle())[0]
        drawable = activityList.getBadgedIcon(0)
    } catch (e: Exception) {
    } catch (e: Error) {
    }

    if (drawable == null) {
        try {
            drawable = context.packageManager.getApplicationIcon(packageName)
        } catch (ignored: Exception) {
        }
    }

    return drawable
}

fun queryAllPackages(context: Context): List<AppEntity> {
    val apps = ArrayList<AppEntity>()
    val intent = createQueryIntent()
    val pm = context.packageManager

    val list = pm.queryIntentActivities(intent, 0)
    for (info in list) {
        val componentInfo = info.activityInfo.applicationInfo

        val label = componentInfo.loadLabel(pm).toString()
        val packageName = componentInfo.packageName
        val icon = getAppIcon(context = context, packageName = packageName) ?: continue

        apps.add(
            AppEntity(
                label = label,
                packageName = packageName,
                icon = icon
            )
        )
    }

    return apps
}

fun openApp(context: Context, packageName: String) {
    context.startActivity(
        context.packageManager.getLaunchIntentForPackage(packageName)
    )
}

fun openAppSettings(context: Context, packageName: String) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri

    context.startActivity(intent)
}

val dateStrFormat = SimpleDateFormat("E dd MMM", Locale.ENGLISH)
val timeStrFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

fun getDateTime(): DateTimeEntity {
    val date = Date()
    val calendar = Calendar.getInstance()

    return DateTimeEntity(
        min = calendar.get(Calendar.MINUTE),
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        dateStr = dateStrFormat.format(date),
        timeStr = timeStrFormat.format(date)
    )
}

fun isDefaultLauncher(context: Context): Boolean {
    val packageName: String = try {
        val info = context.packageManager.resolveActivity(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            },
            PackageManager.MATCH_DEFAULT_ONLY
        )

        info?.activityInfo?.packageName ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }

    return packageName == context.packageName
}

fun createBitmap(
    paths: List<Path>,
    width: Int,
    height: Int,
    brushSize: Float,
    scaledWidth: Int,
    scaledHeight: Int
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.DITHER_FLAG).apply {
        color = Color.Black.toArgb()
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = brushSize
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    canvas.drawColor(android.graphics.Color.WHITE)

    paths.forEach {
        canvas.drawPath(it, paint)
    }

    return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
}
