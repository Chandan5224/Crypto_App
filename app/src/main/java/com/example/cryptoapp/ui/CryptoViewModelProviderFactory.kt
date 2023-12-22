package com.example.cryptoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.repository.CryptoRepository

class CryptoViewModelProviderFactory(private val repository: CryptoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CryptoViewModel(repository) as T
    }
}