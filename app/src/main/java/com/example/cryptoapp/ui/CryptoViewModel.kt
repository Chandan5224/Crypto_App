package com.example.cryptoapp.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.os.IResultReceiver2.Default
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.cryptoapp.R
import com.example.cryptoapp.model.CryptoData
import com.example.cryptoapp.repository.CryptoRepository
import com.example.cryptoapp.util.FileHelper
import com.example.cryptoapp.util.Resource
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CryptoViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val fileHelper: FileHelper,
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    private val _cryptoData: MutableLiveData<Resource<List<CryptoData>>> = MutableLiveData()
    private val _refreshTime: MutableLiveData<String> = MutableLiveData()
    val refreshTime: LiveData<String> get() = _refreshTime
    val cryptoData: LiveData<Resource<List<CryptoData>>> get() = _cryptoData
    private val refreshInterval = 3 * 60 * 1000L // 3 minutes in milliseconds
    private val handler = android.os.Handler()
    private lateinit var refreshRunnable: Runnable
    var checkLogin: MutableLiveData<Boolean> = MutableLiveData()


    init {
        loadSharePreferences()
        scheduleDataRefresh()
        fetchData()
    }

    fun fetchData() {
        _cryptoData.postValue(Resource.Loading())
        viewModelScope.launch {
            _cryptoData.postValue(cryptoRepository.getCombinedCryptoData())
            _refreshTime.postValue(getCurrentTime())
        }
    }


    private fun scheduleDataRefresh() {
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

    private fun loadSharePreferences() {
        if (sharedPreferences.getString("username", "")?.isNotBlank() == true)
            checkLogin.postValue(true)
        else
            checkLogin.postValue(false)

    }

    fun saveUserData(username: String, imageBitmap: Bitmap) {
        sharedPreferences.edit().putString("username", username).apply()
        val imagePath = fileHelper.saveImageToInternalStorage(imageBitmap, "user_image.png")
        sharedPreferences.edit().putString("image_path", imagePath).apply()
        checkLogin.postValue(true)
    }

    fun loadUsername(): String? {
        return sharedPreferences.getString("username", "")
    }

    fun loadImage(): Bitmap? {
        val imagePath = sharedPreferences.getString("image_path", "")
        return if (imagePath.isNullOrEmpty()) {
            null
        } else {
            fileHelper.loadImageFromInternalStorage("user_image.png")
        }
    }

}