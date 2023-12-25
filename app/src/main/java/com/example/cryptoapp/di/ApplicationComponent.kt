package com.example.cryptoapp.di

import android.content.Context
import com.example.cryptoapp.ui.MainActivity
import com.example.cryptoapp.ui.fragment.HomeFragment
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}