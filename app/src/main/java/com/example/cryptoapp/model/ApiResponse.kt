package com.example.cryptoapp.model

data class ApiResponse(
    val success: Boolean,
    val crypto: Map<String, Crypto>,
    val fiat: Map<String, String>
)
