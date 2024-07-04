package com.easyflow.practiceapp.network.network_impl.data_sources

import com.easyflow.practiceapp.domain.models.CurrencyModel
import com.easyflow.practiceapp.network.network_api.data_sources.CurrencyNetworkDataSource
import com.easyflow.practiceapp.network.network_impl.api.CurrencyApi
import com.easyflow.practiceapp.network.network_impl.mappers.mapToCurrencyModel
import javax.inject.Inject

class CurrencyNetworkDataSourceImpl @Inject constructor(
    private val currencyApi: CurrencyApi
): CurrencyNetworkDataSource {

    override suspend fun getCurrencyRates(): CurrencyModel {
        val response = currencyApi.getCurrencyRates()
        return response.mapToCurrencyModel()
    }

}