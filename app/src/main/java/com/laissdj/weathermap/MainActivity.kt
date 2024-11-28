package com.laissdj.weathermap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.laissdj.weathermap.ui.theme.WeatherMapTheme
import com.laissdj.weathermap.view.HomeScreen
import com.laissdj.weathermap.view.WeatherState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherMapTheme {
                Navigator(screen = HomeScreen())

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val weatherInfo = WeatherState.WeatherPresentation(
        isNight = false,
        icon = "https://openweathermap.org/img/wn/02d@2x.png",
        timezone = "São Paulo",
        //country = "BR",
        temp = 25,
        skyDescription = "Clear sky",
        sunrise = "06:00",
        sunset = "18:00",
        hour = "12:00",
        tempMax = 27,
        tempMin = 20,
        rain = 2.5
    )
}