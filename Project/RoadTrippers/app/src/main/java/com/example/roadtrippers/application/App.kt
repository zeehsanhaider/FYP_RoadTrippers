package com.example.roadtrippers.application

import android.app.Application
import com.example.roadtrippers.injection.appModule
import com.example.roadtrippers.util.db.DbReferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // Initializing Firebase Realtime Database References
        DbReferences.initDatabaseReferences()

        // Starting injection
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule)
        }
    }

}