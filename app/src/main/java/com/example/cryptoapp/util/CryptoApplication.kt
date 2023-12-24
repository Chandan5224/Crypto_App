package com.example.cryptoapp.util

import android.app.Application
import com.example.cryptoapp.di.ApplicationComponent
import com.example.cryptoapp.di.DaggerApplicationComponent
import com.example.cryptoapp.di.NetworkModule
import dagger.hilt.android.qualifiers.ApplicationContext

class CryptoApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .build()
    }
}