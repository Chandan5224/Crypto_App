package com.example.cryptoapp.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.cryptoapp.api.CryptoRateApi
import com.example.cryptoapp.util.Constants
import com.example.cryptoapp.util.Constants.Companion.BASE_URL
import com.example.cryptoapp.util.CryptoApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoAPI(retrofit: Retrofit): CryptoRateApi {
        return retrofit.create(CryptoRateApi::class.java)
    }

}