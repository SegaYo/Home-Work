package com.example.homework_6.data.model

data class Daily(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_clear_sky_max: List<Double>,
    val weather_code: List<Int>
)