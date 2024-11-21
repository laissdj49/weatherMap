package com.laissdj.weathermap.repository

import com.laissdj.weathermap.BuildConfig
import com.laissdj.weathermap.response.WeatherResponse
import com.laissdj.weathermap.utils.WeatherMapService

class WeatherRepository(private val service: WeatherMapService) {

    suspend fun getWeather(value: String): WeatherResponse {
        return service.getWeather(
            query = value,
            appid = BuildConfig.WEATHER_API_KEY,
            units = "metric"
        )
    }

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return service.getWeather(
            lat = lat,
            lon = lon,
            appid = BuildConfig.WEATHER_API_KEY,
            units = "metric"
        )

    }
}