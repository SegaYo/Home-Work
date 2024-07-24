package com.example.homework_6.data.api

import com.example.homework_6.data.model.WeatherData
import retrofit2.http.GET

interface WeatherApi {
    @GET("forecast?latitude=51.5406&longitude=46.0086&current=temperature_2m,relative_humidity_2m,apparent_temperature,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m&hourly=visibility&daily=weather_code,temperature_2m_max,temperature_2m_min,uv_index_clear_sky_max&timezone=Europe%2FMoscow&forecast_days=1")
    suspend fun getWeatherInfo(): WeatherData
}