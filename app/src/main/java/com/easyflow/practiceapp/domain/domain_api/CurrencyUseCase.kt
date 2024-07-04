package com.easyflow.practiceapp.domain.domain_api

import com.easyflow.practiceapp.domain.models.CurrencyModel

interface CurrencyUseCase {
    suspend fun getCurrencyRates(): CurrencyModel
}