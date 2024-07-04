package com.easyflow.practiceapp.network.network_api.data_sources

import com.easyflow.practiceapp.domain.models.CurrencyModel

interface CurrencyNetworkDataSource {
    suspend fun getCurrencyRates(): CurrencyModel
}