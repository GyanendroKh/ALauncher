package com.gyanendrokh.alauncher

import android.app.Application
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.PrintStream

class LauncherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

            val crashLogFile = File(
                File(
                    Environment.getExternalStorageDirectory(),
                    Environment.DIRECTORY_DOCUMENTS,
                ),
                "crash.txt"
            )
            PrintStream(crashLogFile).use {
                it.println(e.message)
                it.println()
                e.printStackTrace(it)

                Toast.makeText(
                    this,
                    "Crash log saved to ${crashLogFile.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()
            }

            defaultExceptionHandler?.uncaughtException(t, e)
        }
    }
}
