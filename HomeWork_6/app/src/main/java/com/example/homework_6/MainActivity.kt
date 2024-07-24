package com.example.homework_6

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.homework_6.data.api.WeatherApi
import com.example.homework_6.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val moshiBuilder = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val myCoroutineScope = CoroutineScope(Dispatchers.IO)

    enum class WeatherCondition(val codes: List<Int>, val description: String) {
        CLEAR_SKY(listOf(0), "Clear sky"),
        MAINLY_CLEAR_PARTLY_CLOUDY_OVERCAST(
            listOf(1, 2, 3),
            "Mainly clear, partly cloudy, and overcast"
        ),
        FOG_AND_DEPOSITING_RIME_FOG(listOf(45, 48), "Fog and depositing rime fog"),
        DRIZZLE_LIGHT_MODERATE_DENSE(
            listOf(51, 53, 55),
            "Drizzle: Light, moderate, and dense intensity"
        ),
        FREEZING_DRIZZLE_LIGHT_DENSE(listOf(56, 57), "Freezing Drizzle: Light and dense intensity"),
        RAIN_SLIGHT_MODERATE_HEAVY(
            listOf(61, 63, 65),
            "Rain: Slight, moderate and heavy intensity"
        ),
        FREEZING_RAIN_LIGHT_HEAVY(listOf(66, 67), "Freezing Rain: Light and heavy intensity"),
        SNOW_FALL_SLIGHT_MODERATE_HEAVY(
            listOf(71, 73, 75),
            "Snow fall: Slight, moderate, and heavy intensity"
        ),
        SNOW_GRAINS(listOf(77), "Snow grains"),
        RAIN_SHOWERS_SLIGHT_MODERATE_VIOLENT(
            listOf(80, 81, 82),
            "Rain showers: Slight, moderate, and violent"
        ),
        SNOW_SHOWERS_SLIGHT_HEAVY(listOf(85, 86), "Snow showers slight and heavy"),
        THUNDERSTORM_SLIGHT_MODERATE(listOf(95), "Thunderstorm: Slight or moderate"),
        THUNDERSTORM_WITH_HAIL_SLIGHT_HEAVY(
            listOf(96, 99),
            "Thunderstorm with slight and heavy hail"
        );

        companion object {
            fun fromCode(code: Int): WeatherCondition {
                return values().find { condition -> code in condition.codes }
                    ?: WeatherCondition.CLEAR_SKY
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(this))
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DEV_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
            .build()

        myCoroutineScope.launch {
            val response = retrofit.create(WeatherApi::class.java).getWeatherInfo()
            Log.e("response", response.toString())

            withContext(Dispatchers.Main) {
                binding.tempInfo.text =
                    buildString {
                        append(response.current.temperature_2m.toString())
                        append(response.current_units.temperature_2m)
                    }

                binding.feelsLikeAndSkyState.text = getString(
                    R.string.Feelslike,
                    response.current.apparent_temperature.toString(),
                    response.current_units.apparent_temperature,
                    WeatherCondition.fromCode(
                        response.daily.weather_code.firstOrNull() ?: 0
                    ).description
                )

                binding.minmaxtemp.text = getString(
                    R.string.minmaxtemp,
                    response.daily.temperature_2m_min.toString().replace("[", "").replace("]", ""),
                    response.current_units.temperature_2m,
                    response.daily.temperature_2m_max.toString().replace("[", "").replace("]", ""),
                    response.current_units.temperature_2m
                )

                binding.pressure.text = getString(
                    R.string.pressure,
                    response.current.surface_pressure.toString(),
                    response.current_units.surface_pressure
                )

                binding.humidity.text = getString(
                    R.string.humidity,
                    response.current.relative_humidity_2m.toString(),
                    response.current_units.relative_humidity_2m
                )

                binding.visibility.text = getString(
                    R.string.visibility,
                    ((response.hourly.visibility.firstOrNull() ?: 0) / 1000).toString()
                )

                binding.windspeeed.text = getString(
                    R.string.windspeed,
                    "%.2f".format(response.current.wind_speed_10m / 3.6)
                )

                binding.gust.text = getString(
                    R.string.gust,
                    "%.2f".format(response.current.wind_gusts_10m),
                )
                //Апи даёт только угол
                binding.windDirection.text = getString(
                    R.string.wind_direction,
                    response.current.wind_direction_10m.toString(),
                    response.current_units.wind_direction_10m
                )
            }
        }
    }

    companion object {

        private const val READ_TIMEOUT_IN_SECONDS = 30L
        private const val CONNECTION_TIMEOUT_IN_SECONDS = 30L
        private const val DEV_BASE_URL = "https://api.open-meteo.com/v1/"
    }
}


