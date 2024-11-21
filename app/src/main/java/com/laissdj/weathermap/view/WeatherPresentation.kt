package com.laissdj.weathermap.view

import com.laissdj.weathermap.response.WeatherResponse
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import retrofit2.HttpException
import java.net.UnknownHostException
import kotlin.math.roundToInt


fun WeatherResponse.mapToPresentation(
): WeatherState.WeatherPresentation {
    return WeatherState.WeatherPresentation(
        skyDescription = weather.first().description,
        city = name,
        country = sys.country,
        icon = weather.first().icon,
        rain = rain?.oneHour,
        temp = main.temp.roundToInt(),
        tempMin = main.temp_min.roundToInt(),
        tempMax = main.temp_max.roundToInt(),
        sunrise = getFormattedSunrise(),
        sunset = getFormattedSunset(),
        hour = getFormattedHour(),
        timezone = timezone,
        isNight = isNight(dt, sys.sunset),
        pressure = main.pressure,
        humidity = main.humidity,
        speed = wind.speed.roundToInt(),
        visibility = visibility,
        lat = coord.lat,
        lon = coord.lon

    )
}

fun formatTimestampToDateTime(
    timestamp: Long,
    timeZone: Int,
    showDayOfWeek: Boolean = true
): String {
    val dateTimeFormat = LocalDateTime.Format {
        if (showDayOfWeek) {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
            char(' ')
        }
        hour()
        char(':')
        minute()
    }
    val dateTime = Instant
        .fromEpochSeconds(timestamp)
        .toLocalDateTime(FixedOffsetTimeZone(UtcOffset(seconds = timeZone)))
        .format(dateTimeFormat)
    return dateTime
}

fun isNight(dt: Long, sunset: Long): Boolean {
    val instantDt = Instant.fromEpochMilliseconds(dt)
    val instantSunset = Instant.fromEpochMilliseconds(sunset)
    return instantDt > (instantSunset)
}

sealed class WeatherError(
    val message: String
) {
    data object NotFound : WeatherError("Cidade não encontrada")
    data object NoConnection : WeatherError("Sem conexão com a internet")
    data object Unknown : WeatherError("Erro inesperado")

    companion object {
        fun fromException(exception: Throwable) =
            when {
                exception is HttpException && exception.code() == 404 -> NotFound
                exception is UnknownHostException -> NoConnection
                else -> Unknown

            }
    }
}

sealed interface WeatherState {
    data object Loading : WeatherState
    data object Start : WeatherState
    data class Error(val error: WeatherError) : WeatherState

    data class WeatherPresentation(
        val city: String = "",
        val skyDescription: String = "",
        val icon: String = "",
        val rain: Double? = null,
        val temp: Int = 0,
        val tempMin: Int = 0,
        val tempMax: Int = 0,
        val country: String = "",
        val sunrise: String = "",
        val sunset: String = "",
        val timezone: Int? = 0,
        val hour: String = "",
        val isNight: Boolean = false,
        val pressure: Int = 0,
        val humidity: Int = 0,
        val speed: Int = 0,
        val visibility: Int = 0,
        val lat: Double = 0.0,
        val lon: Double = 0.0
    ) : WeatherState
}