package com.gyanendrokh.alauncher.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gyanendrokh.alauncher.model.AppEntity
import com.gyanendrokh.alauncher.util.queryAllPackages
import kotlinx.coroutines.launch

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    var apps = mutableStateOf<List<AppEntity>>(ArrayList())
    var featuredApps = mutableStateOf<List<AppEntity>>(ArrayList())

    init {
        viewModelScope.launch {
            updateApps()
            updateFeaturedApps()
        }
    }

    private fun updateApps() {
        apps.value = queryAllPackages(
            context = getApplication<Application>().applicationContext
        ).sortedBy {
            it.label
        }
    }

    private fun updateFeaturedApps() {
        val start = (Math.random() * apps.value.size - 6).toInt()

        featuredApps.value = apps.value.subList(
            start,
            start + 6
        )
    }
}
