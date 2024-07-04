package com.easyflow.practiceapp.data.data_impl

import com.easyflow.practiceapp.data.data_api.CurrencyRepository
import com.easyflow.practiceapp.domain.models.CurrencyModel
import com.easyflow.practiceapp.network.network_api.data_sources.CurrencyNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val dataSource: CurrencyNetworkDataSource
): CurrencyRepository {

    override suspend fun getCurrencyRates(): CurrencyModel = withContext(Dispatchers.IO) {
        dataSource.getCurrencyRates()
    }

}