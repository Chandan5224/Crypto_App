package com.example.cryptoapp.model

data class CryptoData(
    val symbol: String = "",
    val name: String = "",
    val name_full: String = "",
    val icon_url: String = "",
    val rate: Double = 0.0
)
