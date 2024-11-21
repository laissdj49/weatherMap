package com.laissdj.weathermap.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.laissdj.weathermap.R.drawable
import kotlinx.serialization.Serializable
import java.util.Locale

//@Composable
//fun Greeting(
//    state: WeatherState,
//    onSearch: (String) -> Unit,
//
//    ) {
//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
//
//    ) {
//        println(state)
//        val drawable = remember(state) {
//
//            if (state is WeatherState.WeatherPresentation && state.isNight) {
//                drawable.image_night1
//            } else {
//                drawable.image_day
//            }
//        }
//
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painterResource(id = drawable),
//            contentDescription = null,
//            contentScale = ContentScale.Crop
//        )
//
//
//        when (state) {
//            WeatherState.Empty -> WeatherInformation(weatherInformation = null)
//            WeatherState.Loading -> CircularProgressIndicator(
//                modifier = Modifier.align(
//                    Alignment.Center
//                ), color = Color.White
//            )
//
//            is WeatherState.WeatherPresentation -> WeatherInformation(
//                Modifier, state
//            )
//        }
//    }
//}

@SuppressLint("DefaultLocale")
@Composable
fun WeatherInformation(
    modifier: Modifier = Modifier,
    weatherInformation: WeatherState.WeatherPresentation?,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {

        if (weatherInformation == null) return
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    tint = Color.White,
                    contentDescription = ""
                )

                Text(
                    modifier = Modifier.padding(start = 2.dp, top = 4.dp),
                    fontSize = 24.sp,
                    text = "${weatherInformation.city} - ${weatherInformation.country}",
                    style = TextStyle(Color.White),
                )
            }
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
                text = weatherInformation.skyDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                },
                style = TextStyle(Color.White),

                )


            Text(
                fontSize = 24.sp,
                text = weatherInformation.hour,
                style = TextStyle(Color.White),
            )

            Text(
                fontSize = 16.sp,
                text = "Max: ${weatherInformation.tempMax}ºC   Min: ${weatherInformation.tempMin}ºC",
                style = TextStyle(Color.White)
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.align(Alignment.BottomStart).padding(top = 16.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val speedKmH = weatherInformation.speed * 3.6

            items(
                listOf(
                    drawable.sunrise_24 to "Sunrise\n ${weatherInformation.sunrise} am",
                    drawable.sunset_24 to "Sunset\n ${weatherInformation.sunset} pm",
                    drawable.pressure_24 to "Pressure\n ${weatherInformation.pressure} mb ",
                    drawable.humidity_24 to "Humidity\n ${weatherInformation.humidity}% ",
                    drawable.visibility_24 to "Visibility\n ${(weatherInformation.visibility) / 1000} km ",
                    drawable.wind_air_24 to "Wind\n ${String.format("%.0f", speedKmH)} km/h"
                )
            ) { (drawable, text) ->
                InfoWeather(
                    image = painterResource(id = drawable),
                    text = text,
                )
            }
        }
    }
    if (weatherInformation?.rain != null) {
        Text(text = "${weatherInformation.rain}mm", style = TextStyle(Color.White))
    }

}

@Composable
fun InfoWeather(image: Painter, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(Color.Blue.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier.padding(start = 4.dp),
                painter = image,
                contentDescription = "",
                alignment = AbsoluteAlignment.CenterLeft
            )

            Text(
                fontSize = 24.sp,
                text = text,
                style = TextStyle(Color.LightGray),
            )
        }

    }
}