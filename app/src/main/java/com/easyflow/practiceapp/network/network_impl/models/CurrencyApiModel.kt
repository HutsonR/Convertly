package com.easyflow.practiceapp.network.network_impl.models

import com.google.gson.annotations.SerializedName

data class CurrencyApiModel (
    @SerializedName("Date")
    val date: String? = null,
    @SerializedName("PreviousDate")
    val previousDate: String? = null,
    @SerializedName("PreviousURL")
    val previousURL: String? = null,
    @SerializedName("Timestamp")
    val timestamp: String? = null,
    @SerializedName("Valute")
    val valute: Map<String, ValuteApiModel>? = null
)