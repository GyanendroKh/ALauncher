package com.gyanendrokh.alauncher.util

import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.graphics.drawable.Drawable
import com.gyanendrokh.alauncher.model.AppEntity

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
