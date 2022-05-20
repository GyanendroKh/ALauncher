package com.gyanendrokh.alauncher.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.util.queryAllPackages
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.max
import kotlin.math.min

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SHARED_PREF_NAME = "APPS"
        private const val SHARED_PREF_HIDDEN_APPS = "HIDDEN_APPS"
    }

    private val featuredAppCount = 7
    private val sharedPreferences: SharedPreferences
    var apps = mutableStateOf<List<AppEntity>>(ArrayList())
    var featuredApps = mutableStateOf<List<AppEntity>>(ArrayList())
    var hiddenApps = mutableStateOf<Set<String>>(HashSet())

    init {
        viewModelScope.launch {
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
            removeAll(apps.toSet())
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
            it.label.lowercase()
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
