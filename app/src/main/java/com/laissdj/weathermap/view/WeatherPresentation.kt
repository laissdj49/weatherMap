package com.laissdj.weathermap.view

import com.laissdj.weathermap.response.WeatherResponse
import kotlin.math.roundToInt


class WeatherMapDomain() {

    fun mapToPresentation(
        weatherResponse: WeatherResponse
    ): WeatherPresentation {
        return WeatherPresentation(
            skyDescription = weatherResponse.weather.first().description,
            icon = weatherResponse.weather.first().icon,
            rain = weatherResponse.rain?.oneHour,
            temp = weatherResponse.main.temp.roundToInt(),
            tempMin = weatherResponse.main.temp_min.roundToInt(),
            tempMax = weatherResponse.main.temp_max.roundToInt()

        )
    }
}

data class WeatherPresentation(
    val skyDescription: String = "",
    val icon: String = "",
    val rain: Double? = null,
    val temp: Int = 0,
    val tempMin: Int = 0,
    val tempMax: Int = 0,
)