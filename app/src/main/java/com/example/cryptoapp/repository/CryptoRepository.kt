package com.example.cryptoapp.repository

import com.example.cryptoapp.api.RetrofitInstance

class CryptoRepository {
    suspend fun getCryptoRates() = RetrofitInstance.api.getCryptoRates()

    suspend fun getCryptoData() = RetrofitInstance.api.getCryptoData()
}