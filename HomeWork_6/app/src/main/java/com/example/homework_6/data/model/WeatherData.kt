package com.example.homework_6.data.model

data class WeatherData(
    val current: Current,
    val current_units: CurrentUnits,
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Int,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)