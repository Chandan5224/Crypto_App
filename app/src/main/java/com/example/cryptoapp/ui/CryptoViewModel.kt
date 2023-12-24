package com.example.cryptoapp.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.cryptoapp.model.Crypto
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.repository.CryptoRepository
import com.example.cryptoapp.util.NetworkConnection
import com.example.cryptoapp.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CryptoViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    private val _cryptoData: MutableLiveData<Resource<List<CryptoData>>> = MutableLiveData()
    private val _refreshTime: MutableLiveData<String> = MutableLiveData()
    val refreshTime: LiveData<String> get() = _refreshTime
    val cryptoData: LiveData<Resource<List<CryptoData>>> get() = _cryptoData

    private val refreshInterval = 3 * 60 * 1000L // 3 minutes in milliseconds
    private val handler = android.os.Handler()
    private lateinit var refreshRunnable: Runnable

    init {
        scheduleDataRefresh()
//        fetchData()
    }

    fun fetchData() {
        _cryptoData.postValue(Resource.Loading())
        viewModelScope.launch {
            _cryptoData.postValue(cryptoRepository.getCombinedCryptoData())
            _refreshTime.postValue(getCurrentTime())
        }
    }


    fun scheduleDataRefresh() {
        refreshRunnable = object : Runnable {
            override fun run() {
                fetchData()
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