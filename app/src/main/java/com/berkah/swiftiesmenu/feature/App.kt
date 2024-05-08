package com.berkah.swiftiesmenu.feature

import android.app.Application
import com.berkah.swiftiesmenu.feature.data.source.local.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}
