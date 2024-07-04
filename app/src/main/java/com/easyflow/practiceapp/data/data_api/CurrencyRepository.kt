package com.easyflow.practiceapp.data.data_api

import com.easyflow.practiceapp.domain.models.CurrencyModel

interface CurrencyRepository {
    suspend fun getCurrencyRates(): CurrencyModel
}