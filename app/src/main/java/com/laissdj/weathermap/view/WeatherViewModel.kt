package com.laissdj.weathermap.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laissdj.weathermap.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherMap: MutableStateFlow<WeatherPresentation> =
        MutableStateFlow(WeatherPresentation())
    val weatherMap: StateFlow<WeatherPresentation> = _weatherMap

    fun weatherMap(value: String) {
        viewModelScope.launch {
            runCatching {
                val result = repository.getWeather(value)
                val domain = WeatherMapDomain()
                _weatherMap.value = domain.mapToPresentation(result)
            }
        }
    }
}