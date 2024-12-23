package com.gyanendrokh.alauncher.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.util.queryAllPackages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SHARED_PREF_NAME = "APPS"
        private const val SHARED_PREF_HIDDEN_APPS = "HIDDEN_APPS"
        const val FEATURED_APP_COUNT = 7
    }

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        SHARED_PREF_NAME,
        Context.MODE_PRIVATE
    )

    private val _apps = MutableStateFlow<List<AppEntity>>(listOf())
    val apps: StateFlow<List<AppEntity>> = _apps

    private val _hiddenApps = MutableStateFlow<Set<String>>(setOf())
    val hiddenApps: StateFlow<Set<String>> = _hiddenApps

    private val _filteredApps = MutableStateFlow<List<AppEntity>>(listOf())
    val filteredApps: StateFlow<List<AppEntity>> = _filteredApps

    private val _featuredApps = mutableStateOf<List<AppEntity>>(listOf())
    val featuredApps: State<List<AppEntity>> = _featuredApps

    init {
        val hApps = sharedPreferences.getString(SHARED_PREF_HIDDEN_APPS, "")?.split(";")

        if (hApps != null) {
            _hiddenApps.value = HashSet<String>().apply {
                addAll(hApps)
            }
        }

        viewModelScope.launch {
            apps.collect {
                _filteredApps.value = it.filter { a ->
                    !hiddenApps.value.contains(a.packageName)
                }
            }
        }

        viewModelScope.launch {
            hiddenApps.collect {
                _filteredApps.value = apps.value.filter { a ->
                    !it.contains(a.packageName)
                }
            }
        }

        viewModelScope.launch {
            filteredApps.collect { f ->
                if (f.isEmpty()) {
                    return@collect
                }

                val idxs = ArrayList<Int>()
                var count = 0

                while (true) {
                    val idx = (Math.random() * f.size).toInt()

                    if (!idxs.contains(idx) && hApps?.contains(f[idx].packageName) != true) {
                        idxs.add(idx)
                        count++
                    }

                    if (count == FEATURED_APP_COUNT) break
                }

                _featuredApps.value = idxs.map { f[it] }
            }
        }

        viewModelScope.launch {
            updateApps()
        }
    }

    fun addHidden(apps: List<String>) {
        _hiddenApps.value = HashSet<String>().apply {
            addAll(hiddenApps.value)
            addAll(apps)
        }

        sharedPreferences
            .edit()
            .putString(SHARED_PREF_HIDDEN_APPS, hiddenApps.value.joinToString(";"))
            .apply()
    }

    fun removeHidden(apps: List<String>) {
        _hiddenApps.value = HashSet<String>().apply {
            addAll(hiddenApps.value)
            removeAll(apps.toSet())
        }

        sharedPreferences
            .edit()
            .putString(SHARED_PREF_HIDDEN_APPS, hiddenApps.value.joinToString(";"))
            .apply()
    }

    suspend fun updateApps() {
        withContext(Dispatchers.IO) {
            _apps.value = queryAllPackages(context = getApplication())
                .toSet()
                .sortedBy {
                    it.label.lowercase()
                }
        }
    }
}
