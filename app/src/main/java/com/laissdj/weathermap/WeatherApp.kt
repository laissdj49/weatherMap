package com.laissdj.weathermap

import android.app.Application
import com.laissdj.weathermap.repository.WeatherRepository
import com.laissdj.weathermap.utils.RetrofitBuild
import com.laissdj.weathermap.utils.WeatherMapService
import com.laissdj.weathermap.view.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

class WeatherApp: Application() {

    val module = module {
        single { RetrofitBuild.retrofit  }
        single { get<Retrofit>().create(WeatherMapService::class.java) }
        singleOf(::WeatherRepository)
        viewModelOf(::WeatherViewModel)
    }

   override fun onCreate(){
       super.onCreate()
       startKoin {
           // Log Koin into Android logger
           androidLogger()
           // Reference Android context
           androidContext(this@WeatherApp)
           // Load modules
           modules(module)
       }
   }

}