package com.easyflow.practiceapp.domain.models

data class ValuteModel(
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previous: Double
)
