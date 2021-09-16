package com.gyanendrokh.alauncher.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.util.queryAllPackages
import kotlin.math.max
import kotlin.math.min

// TODO: Implement LiveData or RxJava2 or Flow for State

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    private val SHARED_PREF_NAME = "APPS"
    private val SHARED_PREF_HIDDEN_APPS = "HIDDEN_APPS"
    private val handler = Handler(Looper.myLooper()!!)
    private val featuredAppCount = 7
    private val sharedPreferences: SharedPreferences
    var apps = mutableStateOf<List<AppEntity>>(ArrayList())
    var featuredApps = mutableStateOf<List<AppEntity>>(ArrayList())
    var hiddenApps = mutableStateOf<MutableSet<String>>(HashSet())

    init {
        handler.post {
            updateApps()
            updateFeaturedApps()
        }

        sharedPreferences = application.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val hApps = sharedPreferences.getString(SHARED_PREF_HIDDEN_APPS, "")?.split(";")
        if (hApps != null) {
            hiddenApps.value = HashSet<String>().apply {
                addAll(hApps)
            }
        }
    }

    fun addHidden(apps: List<String>) {
        hiddenApps.value = HashSet<String>().apply {
            addAll(hiddenApps.value)
            addAll(apps)
        }

        sharedPreferences
            .edit()
            .putString(SHARED_PREF_HIDDEN_APPS, hiddenApps.value.joinToString(";"))
            .apply()
    }

    fun removeHidden(apps: List<String>) {
        hiddenApps.value = HashSet<String>().apply {
            addAll(hiddenApps.value)
            removeAll(apps)
        }

        sharedPreferences
            .edit()
            .putString(SHARED_PREF_HIDDEN_APPS, hiddenApps.value.joinToString(";"))
            .apply()
    }

    fun updateApps() {
        apps.value = queryAllPackages(
            context = getApplication<Application>().applicationContext
        ).sortedBy {
            it.label
        }
    }

    fun updateFeaturedApps() {
        val start = min(
            max(
                (Math.random() * apps.value.size - featuredAppCount).toInt(),
                0
            ),
            apps.value.size - featuredAppCount
        )

        featuredApps.value = apps.value.filter {
            !hiddenApps.value.contains(it.packageName)
        }.subList(
            start,
            start + featuredAppCount
        )
    }
}
