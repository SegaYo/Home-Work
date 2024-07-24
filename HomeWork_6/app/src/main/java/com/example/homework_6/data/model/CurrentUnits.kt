package com.example.homework_6.data.model

data class CurrentUnits(
    val apparent_temperature: String,
    val interval: String,
    val relative_humidity_2m: String,
    val surface_pressure: String,
    val temperature_2m: String,
    val time: String,
    val wind_direction_10m: String,
    val wind_gusts_10m: String,
    val wind_speed_10m: String
)