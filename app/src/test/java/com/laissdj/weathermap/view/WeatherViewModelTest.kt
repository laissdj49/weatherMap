package com.laissdj.weathermap.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.laissdj.weathermap.repository.WeatherRepository
import com.laissdj.weathermap.response.Coord
import com.laissdj.weathermap.response.Main
import com.laissdj.weathermap.response.Sys
import com.laissdj.weathermap.response.Weather
import com.laissdj.weathermap.response.WeatherResponse
import com.laissdj.weathermap.response.Wind
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class WeatherViewModelTest {

    @get:Rule
    val instantExecutorRule =
        InstantTaskExecutorRule() //permite LiveData execução instantânea em testes
    private lateinit var viewModel: WeatherViewModel
    private val repository: WeatherRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @Test
    fun `weatherMap should return WeatherPresentation when repository returns data`() = runTest {
        coEvery { repository.getWeather(any()) } returns response
//throws
        viewModel.weatherMap("Aracaju")
        testDispatcher.scheduler.advanceUntilIdle() // Espera a execução da coroutine

        val state = viewModel.weatherMap.value

        assertTrue(state is WeatherState.WeatherPresentation)
        assertEquals(presentation, state)

    }
    @Test
    fun `weatherLocation should return WeatherPresentation when repository returns data`() = runTest {
        coEvery { repository.getWeather(any(), any()) } returns response

        viewModel.weatherLocation(-10.9111, -37.0717)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherMap.value

        assertTrue(state is WeatherState.WeatherPresentation)
        assertEquals(presentation,state)

    }

    val error = WeatherState.Error(
        error = WeatherError.NotFound
    )

    @Test
    fun `weatherMap should return Error state when repository returns null`() = runTest {
        coEvery { repository.getWeather(any()) } throws HttpException(Response.error<WeatherResponse>(404,"".toResponseBody()))

        viewModel.weatherMap("cidade não encontrada")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherMap.value as WeatherState.Error

        assertEquals(WeatherError.NotFound, state.error)
    }

    @Test
    fun `weatherMap ssss`() = runTest {
        coEvery { repository.getWeather(any()) } throws UnknownHostException()

        viewModel.weatherMap("Sem conexão com a internet")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.weatherMap.value as WeatherState.Error

        assertEquals(WeatherError.NoConnection, state.error)
    }

    val presentation = WeatherState.WeatherPresentation(
        city = "Aracaju",
        skyDescription = "scattered clouds",
        icon = "03d",
        rain = null,
        temp = 28,
        tempMin = 28,
        tempMax = 28,
        country = "BR",
        sunrise = "05:06",
        sunset = "17:23",
        timezone = -10800,
        hour = "Thu 09:54",
        isNight = false,
        pressure = 1014,
        humidity = 74,
        speed = 5,
        visibility = 10000,
        lat = -10.9111,
        lon = -37.0717
    )
    val response = WeatherResponse(
        coord = Coord(lon = -37.0717, lat = -10.9111),
        weather = listOf(
            Weather(
                id = 802,
                main = "Clouds",
                description = "scattered clouds",
                icon = "03d"
            )
        ),
        main = Main(
            temp = 27.97,
            temp_min = 27.97,
            temp_max = 27.97,
            feels_like = 31.15,
            pressure = 1014,
            humidity = 74,
            sea_level = 1014,
            grnd_level = 1012
        ),
        visibility = 10000,
        wind = Wind(speed = 5.14, deg = 60),
        dt = 1728564886,
        sys = Sys(
            type = 1,
            id = 8322,
            country = "BR",
            sunrise = 1728547578,
            sunset = 1728591823),
        timezone = -10800,
        rain = null,
        name = "Aracaju",
        cod = 200

    )
}
//    {
//        "coord":{ "lon":-37.0717, "lat":-10.9111 }, "weather":[{ "id":802, "main":"Clouds", "description":"scattered clouds", "icon":"03d" }],
//        "base":"stations", "main":{ "temp":27.97, "feels_like":31.15, "temp_min":27.97, "temp_max":27.97, "pressure":1014,
//        "humidity":74, "sea_level":1014, "grnd_level":1012 },
//        "visibility":10000, "wind":{ "speed":5.14, "deg":60 }, "clouds":{ "all":40 }, "dt":1728564886,
//        "sys":{ "type":1, "id":8322, "country":"BR", "sunrise":1728547578, "sunset":1728591823 },
//        "timezone":-10800, "id":3471872, "name":"Aracaju", "cod":200
//    }
//    WeatherPresentation(city=Aracaju, skyDescription=scattered clouds, icon=03d, rain=null, temp=28, tempMin=28,
//    tempMax=28, country=BR, sunrise=05:06,
//    sunset=17:23, timezone=-10800, hour=Thu 09:54, isNight=false,
//    pressure=1014, humidity=74, speed=5, visibility=10000, lat=-10.9111, lon=-37.0717)


//{"cod":"404","message":"city not found"}