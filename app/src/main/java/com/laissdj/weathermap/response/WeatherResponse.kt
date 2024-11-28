package com.laissdj.weathermap.response

import com.laissdj.weathermap.view.formatTimestampToDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    //val coord: Coord,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: Current,
    val weather: List<Weather>,
    val minutely: List<Minutely>,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    val alerts: List<Alerts>,
//    val base: String,
    //val main: Main,
//    val visibility: Int,
//    val wind: Wind,
//    val rain: Rain? = null,
//    val clouds: Clouds,
//    val dt: Long,
//    val sys: Sys,
    //val timezone: Int,
//    val id: Int,
//    val name: String,
//    val cod: Int
) {
    fun getFormattedHour(): String = formatTimestampToDateTime(daily.first().dt, timezone_offset)
    fun getFormattedSunrise(): String =
        formatTimestampToDateTime(daily.first().sunrise, timezone_offset, showDayOfWeek = false)

    fun getFormattedSunset(): String =
        formatTimestampToDateTime(daily.first().sunset, timezone_offset, showDayOfWeek = false)


}

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)

@Serializable
data class Current(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Int,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<Weather>

)

@Serializable
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class Minutely(
    val dt: Long,
    val precipitation: Int
)

@Serializable
data class Hourly(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
//    val temp_min: Double,
//    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Int,
    val clouds: Int,
    val visibility: Int,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<Weather>,
    val pop: Double
//    val sea_level: Int? = null,
//    val grnd_level: Int? = null
)

@Serializable
data class Daily(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Double,
    val summary: String,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val uvi: Double
)

@Serializable
data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@Serializable
data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

@Serializable
data class Alerts(
    val sender_name: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    val tags: List<String>
)


@Serializable
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

@Serializable
data class Rain(
    @SerialName("1h")
    val oneHour: Double? = null,
    @SerialName("3h")
    val threeHour: Double? = null
)

@Serializable
data class Clouds(
    val all: Int
)

@Serializable
data class Sys(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)
