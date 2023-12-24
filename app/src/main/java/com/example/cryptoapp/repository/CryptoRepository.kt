package com.example.cryptoapp.repository

import com.example.cryptoapp.api.CryptoRateApi
import com.example.cryptoapp.model.ApiResponse
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.model.CryptoRateResponse
import com.example.cryptoapp.util.Resource
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class CryptoRepository @Inject constructor(private val cryptoRateApi: CryptoRateApi) {

    private suspend fun getCryptoRates() = cryptoRateApi.getCryptoRates()

    private suspend fun getCryptoData() = cryptoRateApi.getCryptoData()
    suspend fun getCombinedCryptoData(): Resource<List<CryptoData>> {
        return try {
            val nameImageUrlResponse = getCryptoData()
            val rateResponse = getCryptoRates()
            combineData(nameImageUrlResponse, rateResponse)
        } catch (e: UnknownHostException) {
            Resource.Error("UnknownHostException")
        } catch (e: Exception) {
            Resource.Error("Data can't fetch some errors occur")
        }
    }

    private fun combineData(
        nameImageUrlResponse: Response<ApiResponse>,
        rateResponse: Response<CryptoRateResponse>
    ): Resource<List<CryptoData>> {
        val combinedData = mutableListOf<CryptoData>()
        var cryptoDataResponse = mutableMapOf<String, CryptoData>()

        if (nameImageUrlResponse.isSuccessful && rateResponse.isSuccessful) {
            nameImageUrlResponse.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    cryptoDataResponse = resultResponse.crypto.toMutableMap()
                }
            }
            rateResponse.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    for ((key, value) in resultResponse.rates) {
                        val cryptoData = CryptoData(
                            name_full = cryptoDataResponse[key]!!.name_full,
                            icon_url =  cryptoDataResponse[key]!!.icon_url,
                            rate = String.format("%.6f", value).toDouble()
                        )
                        combinedData.add(cryptoData)
                    }
                }
            }
            return Resource.Success(combinedData)
        }
        return Resource.Error("Data can't fetch some errors occur")
    }
}