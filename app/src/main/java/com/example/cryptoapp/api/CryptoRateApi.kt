package com.example.cryptoapp.api

import com.example.cryptoapp.model.ApiResponse
import com.example.cryptoapp.model.CryptoRateResponse
import com.example.cryptoapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoRateApi {
    @GET("live")
    suspend fun getCryptoRates(@Query("access_key") access_key: String = API_KEY): Response<CryptoRateResponse>

    @GET("list")
    suspend fun getCryptoData(@Query("access_key") access_key: String = API_KEY): Response<ApiResponse>
}