package com.example.cryptoapp.repository

import com.example.cryptoapp.api.RetrofitInstance
import com.example.cryptoapp.model.ApiResponse
import com.example.cryptoapp.model.Crypto
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.model.CryptoRateResponse
import retrofit2.Response

class CryptoRepository {

    suspend fun getCryptoRates() = RetrofitInstance.api.getCryptoRates()

    suspend fun getCryptoData() = RetrofitInstance.api.getCryptoData()
    suspend fun getCombinedCryptoData(): List<CryptoData> {
        val nameImageUrlResponse = getCryptoData()
        val rateResponse = getCryptoRates()
        return combineData(nameImageUrlResponse, rateResponse)
    }

    private fun combineData(
        nameImageUrlResponse: Response<ApiResponse>,
        rateResponse: Response<CryptoRateResponse>
    ): List<CryptoData> {
        val combinedData = mutableListOf<CryptoData>()
        var cryptoDataResponse = mutableMapOf<String, Crypto>()

        if (nameImageUrlResponse.isSuccessful && rateResponse.isSuccessful) {
            nameImageUrlResponse.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    cryptoDataResponse=resultResponse.crypto.toMutableMap()
                }
            }
            rateResponse.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    for ((key, value) in resultResponse.rates) {
                        val cryptoData = CryptoData(
                            cryptoDataResponse[key]!!.name_full,
                            cryptoDataResponse[key]!!.icon_url,
                            String.format("%.6f", value).toDouble()
                        )
                        combinedData.add(cryptoData)
                    }
                }
            }
        }
        return combinedData
    }
}