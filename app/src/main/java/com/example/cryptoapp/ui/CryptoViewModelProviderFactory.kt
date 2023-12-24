package com.example.cryptoapp.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.repository.CryptoRepository
import javax.inject.Inject

class CryptoViewModelProviderFactory @Inject constructor(
    private val repository: CryptoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CryptoViewModel(
            repository
        ) as T
    }
}