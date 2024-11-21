package com.laissdj.weathermap.utils

import com.laissdj.weathermap.BuildConfig.WEATHER_API_KEY
import com.laissdj.weathermap.response.WeatherResponse

class WeatherService {

    private val service by lazy {
        RetrofitBuild.build<WeatherMapService>()
    }

    suspend fun getWeather(query: String): WeatherResponse {
        println("fazendo requisição")
       val response = service.getWeather(query = query, appid = WEATHER_API_KEY)
        println(response.toString())
        return response
    }
    suspend fun getZipCode(){}

}