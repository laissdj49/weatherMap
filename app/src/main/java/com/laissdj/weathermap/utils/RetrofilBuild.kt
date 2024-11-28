package com.laissdj.weathermap.utils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


object RetrofitBuild {

    private val contentType = "application/json".toMediaType()

    private val json = Json {
        ignoreUnknownKeys = true
        this.isLenient = true
    }

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
   private val client  = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val retrofit: Retrofit by lazy {
         Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/3.0/")
             .client(client)
             .addConverterFactory(json.asConverterFactory(contentType))
             .build()
    }

    inline fun <reified T> build(): T =
        retrofit.create(T::class.java)
}



//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid={API key}