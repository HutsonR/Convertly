package com.easyflow.practiceapp.domain.models

data class CurrencyModel (
    val date: String,
    val previousDate: String,
    val previousURL: String,
    val timestamp: String,
    val valute: Map<String, ValuteModel>
)