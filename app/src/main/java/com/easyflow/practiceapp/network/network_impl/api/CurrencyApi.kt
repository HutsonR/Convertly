package com.easyflow.practiceapp.network.network_impl.api

import com.easyflow.practiceapp.network.network_impl.models.CurrencyApiModel
import retrofit2.http.GET

interface CurrencyApi {

    @GET("daily_json.js")
    suspend fun getCurrencyRates(): CurrencyApiModel

}