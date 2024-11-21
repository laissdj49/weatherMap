package com.laissdj.weathermap.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.laissdj.weathermap.R
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
class WeatherScreen(private val query: WeatherQuery) : Screen {

    @Composable
    override fun Content() {

        val viewModel: WeatherViewModel = koinViewModel<WeatherViewModel>()
        val state by viewModel.weatherMap.collectAsState()
        val navigator = LocalNavigator.currentOrThrow


        LaunchedEffect(query) {
            loadingWeather(query, viewModel)
        }

        Box {
            println(state)
            val drawable = remember(state) {

                if (state is WeatherState.WeatherPresentation &&
                    (state as WeatherState.WeatherPresentation).isNight
                ) {
                    R.drawable.image_night1
                } else {
                    R.drawable.image_day
                }
            }

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            when (state) {
                WeatherState.Start -> Unit

                WeatherState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Carregando...", color = Color.White)
                    }
                }

                is WeatherState.WeatherPresentation -> WeatherInformation(
                    Modifier, state as WeatherState.WeatherPresentation,
                )

                is WeatherState.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val errorState = state as WeatherState.Error
                        Text(
                            modifier = Modifier.padding(bottom = 16.dp),
                            text = errorState.error.message,
                            color = Color.White

                        )
                        Button(onClick = {
                            when (errorState.error) {
                                WeatherError.NoConnection -> loadingWeather(query, viewModel)
                                else -> navigator.pop()
                            }
                        }) {
                            Text("Tentar novamente")
                        }
                    }

                    //Text(text = (state as WeatherState.Error).message, color = Color.Red)
                }

//                WeatherState.NavigateToSearch -> navigator.push(
//                    WeatherScreen(
//                        WeatherQuery.Text(
//                            query.toString()
//                        )
//                    )
//                )
            }
        }
    }
}


fun loadingWeather(
    query: WeatherQuery,
    viewModel: WeatherViewModel
) {
    when (query) {
        is WeatherQuery.Location -> viewModel.weatherLocation(query.lat, query.lon)
        is WeatherQuery.Text -> viewModel.weatherMap(query.text)
    }
}

@Serializable
sealed class WeatherQuery {
    data class Text(
        val text: String
    ) : WeatherQuery()

    data class Location(
        val lat: Double,
        val lon: Double
    ) : WeatherQuery()
}