package com.example.cryptoapp.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.model.Crypto
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.repository.CryptoRepository
import com.example.cryptoapp.util.Resource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CryptoViewModel(private val cryptoRepository: CryptoRepository) : ViewModel() {
    private val cryptoRatesResponse = mutableMapOf<String, Double>()
    private val cryptoDataResponse = mutableMapOf<String, Crypto>()
    val cryptoData: MutableLiveData<Resource<MutableList<CryptoData>>> = MutableLiveData()
    val refreshTime: MutableLiveData<Resource<String>> = MutableLiveData()

    private val refreshInterval = 3 * 60 * 1000L // 3 minutes in milliseconds
    private val handler = android.os.Handler()
    private lateinit var refreshRunnable: Runnable

    init {
        reFetchData()
        scheduleDataRefresh()
    }

    private fun getCryptoRates() = viewModelScope.launch {
        cryptoData.postValue(Resource.Loading())
        val response = cryptoRepository.getCryptoRates()
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    for ((key, value) in resultResponse.rates)
                        cryptoRatesResponse[key] = value
                }
            }
        } else {
            cryptoData.postValue(Resource.Error(response.message()))
        }

    }

    private fun getCryptoData() = viewModelScope.launch {
        val response = cryptoRepository.getCryptoData()
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.success) {
                    for ((key, value) in resultResponse.crypto)
                        cryptoDataResponse[key] = value
                    combineData()
                }
            }
        } else {
            cryptoData.postValue(Resource.Error(response.message()))
        }
    }

    private fun combineData() {
        val list = mutableListOf<CryptoData>()
        for ((key, value) in cryptoRatesResponse) {
            cryptoDataResponse[key]?.let { crypto ->
                val data = CryptoData(
                    crypto.name_full,
                    crypto.icon_url,
                    String.format("%.6f", value).toDouble()
                )
                list.add(data)
            }
        }
        cryptoData.postValue(Resource.Success(list))
    }


    fun reFetchData() {
        getCryptoRates()
        getCryptoData()
        refreshTime.postValue(Resource.Success(getCurrentTime()))
    }

    private fun scheduleDataRefresh() {
        refreshRunnable = object : Runnable {
            override fun run() {
                reFetchData()
                handler.postDelayed(this, refreshInterval)
            }
        }
        handler.postDelayed(refreshRunnable, refreshInterval)
    }

    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(currentTime)
    }
}