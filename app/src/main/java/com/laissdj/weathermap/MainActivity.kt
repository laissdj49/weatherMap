package com.laissdj.weathermap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.laissdj.weathermap.ui.theme.WeatherMapTheme
import com.laissdj.weathermap.view.WeatherPresentation
import com.laissdj.weathermap.view.WeatherViewModel
import org.koin.android.ext.android.inject

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by inject<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val weather by viewModel.weatherMap.collectAsState()
            WeatherMapTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var value by remember {
                        mutableStateOf("")
                    }
                    Greeting(
                        weatherInformation = weather,
                        value = value,
                        onValueChange = { value = it })

                    LaunchedEffect(value) {
                        viewModel.weatherMap("Barcelona")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    weatherInformation: WeatherPresentation,
    value: String,
    onValueChange: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.image_night1),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(50.dp,6.dp)) {

           // TextField(value = value, onValueChange = onValueChange)

            AsyncImage(
                modifier = Modifier.size(150.dp),
                model = "https://openweathermap.org/img/wn/${weatherInformation.icon}@2x.png",
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(
               fontSize = 50.sp,
                text = "${weatherInformation.temp}ºC",
                style = TextStyle(Color.White)
            )

            Text(
                fontSize = 24.sp,
                text = weatherInformation.skyDescription,
                style = TextStyle(Color.White),

            )
            Text(
                fontSize = 20.sp,
                text = "Max: ${weatherInformation.tempMax}ºC   Min: ${weatherInformation.tempMin}ºC",
                style = TextStyle(Color.White)
            )
            Text(text = weatherInformation.rain.toString(), style = TextStyle(Color.White))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherMapTheme {
        //Greeting(modifier = Modifier, weatherInformation = WeatherPresentation())
    }
}