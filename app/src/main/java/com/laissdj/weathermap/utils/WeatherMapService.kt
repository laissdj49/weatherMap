package com.laissdj.weathermap.utils

import com.laissdj.weathermap.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherMapService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

}