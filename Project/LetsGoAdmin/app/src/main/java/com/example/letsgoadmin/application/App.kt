package com.example.letsgoadmin.application

import android.app.Application
import com.example.letsgoadmin.injection.appModule
import com.example.letsgoadmin.util.db.DbReferences
import com.example.letsgoadmin.util.storage.StorageReferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initializing Firebase Realtime Database References
        DbReferences.initDatabaseReferences()

        // Initializing Firebase Storage References
        StorageReferences.initStorageReferences()

        // Starting injection
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModule)
        }
    }

}