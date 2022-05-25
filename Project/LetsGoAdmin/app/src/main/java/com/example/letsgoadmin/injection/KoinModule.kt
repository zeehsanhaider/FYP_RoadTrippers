package com.example.letsgoadmin.injection

import com.example.letsgoadmin.database.local.UserDatabase
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.util.preference.RememberMePreference
import com.example.letsgoadmin.util.toast.ToastUtil
import com.example.smartretailadmin.storage.controller.StorageController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    // Provides connection to firebase real-time database
    factory { DatabaseController() }

    // Provides helper functions for displaying elegant toast messages
    single { ToastUtil(androidContext()) }

    // Provides remember me functionality
    single { RememberMePreference(androidContext()) }

    // Provides local database for saving curr user data
    single { UserDatabase(androidContext()) }


    // Provides data lists for products, users and orders
//    single { DataGenerator() }

    // Provides apps firebase storage access and operations too
    factory { StorageController(androidContext()) }

    // Provides local database for maintaining cart of a user
//    single { CartDatabase(androidContext()) }

}
