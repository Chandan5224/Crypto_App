package com.example.cryptoapp.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.cryptoapp.api.CryptoRateApi
import com.example.cryptoapp.util.Constants
import com.example.cryptoapp.util.FileHelper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoAPI(retrofit: Retrofit): CryptoRateApi {
        return retrofit.create(CryptoRateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideFileHelper(): FileHelper {
        return FileHelper(application)
    }
}