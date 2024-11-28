package com.laissdj.weathermap.utils

import com.laissdj.weathermap.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherMapService {

    @GET("onecall")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse


}