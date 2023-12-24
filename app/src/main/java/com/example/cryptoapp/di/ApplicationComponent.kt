package com.example.cryptoapp.di

import android.content.Context
import com.example.cryptoapp.ui.MainActivity
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)

}