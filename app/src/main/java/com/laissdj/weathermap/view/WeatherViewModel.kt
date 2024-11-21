package com.laissdj.weathermap.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laissdj.weathermap.repository.WeatherRepository
import com.laissdj.weathermap.view.WeatherError.Companion.fromException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherMap: MutableStateFlow<WeatherState> =
        MutableStateFlow(WeatherState.Start)
    val weatherMap: StateFlow<WeatherState> = _weatherMap

    fun weatherMap(value: String) {
        viewModelScope.launch {
            runCatching {
                _weatherMap.value = WeatherState.Loading


                val response = repository.getWeather(value)
                _weatherMap.value = response.mapToPresentation()

            }.onFailure { e ->
                e.printStackTrace()
                _weatherMap.value = WeatherState.Error(fromException(exception = e))
            }
        }
    }

    fun weatherLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            runCatching {
                _weatherMap.value = WeatherState.Loading
                val response = repository.getWeather(lat, lon)
                _weatherMap.value = response.mapToPresentation()
            }.onFailure { e ->
                e.printStackTrace()
                _weatherMap.value = WeatherState.Error(fromException(e))
            }
        }
    }
}