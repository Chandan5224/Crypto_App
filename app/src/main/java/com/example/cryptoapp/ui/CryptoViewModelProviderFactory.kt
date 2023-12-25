package com.example.cryptoapp.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.repository.CryptoRepository
import com.example.cryptoapp.util.FileHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CryptoViewModelProviderFactory @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val fileHelper: FileHelper,
    private val repository: CryptoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CryptoViewModel(
            sharedPreferences,
            fileHelper,
            repository
        ) as T
    }
}