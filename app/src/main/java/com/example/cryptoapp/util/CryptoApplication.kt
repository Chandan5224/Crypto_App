package com.example.cryptoapp.util

import android.app.Application
import com.example.cryptoapp.di.AppModule
import com.example.cryptoapp.di.ApplicationComponent
import com.example.cryptoapp.di.DaggerApplicationComponent

class CryptoApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}