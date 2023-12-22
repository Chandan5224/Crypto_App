package com.example.cryptoapp.model

data class CryptoRateResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val target: String,
    val rates: Map<String, Double>
)
