package com.example.homework_6.data.model

data class Current(
    val apparent_temperature: Double,
    val interval: Int,
    val relative_humidity_2m: Int,
    val surface_pressure: Double,
    val temperature_2m: Double,
    val time: String,
    val wind_direction_10m: Double,
    val wind_gusts_10m: Double,
    val wind_speed_10m: Double
)