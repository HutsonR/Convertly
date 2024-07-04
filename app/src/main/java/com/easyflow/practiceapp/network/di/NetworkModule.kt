package com.easyflow.practiceapp.network.di

import com.easyflow.practiceapp.network.network_api.data_sources.CurrencyNetworkDataSource
import com.easyflow.practiceapp.network.network_impl.api.CurrencyApi
import com.easyflow.practiceapp.network.network_impl.data_sources.CurrencyNetworkDataSourceImpl
import com.easyflow.practiceapp.network.network_impl.models.CurrencyApiModel
import com.easyflow.practiceapp.network.network_impl.providers.NetworkProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getCurrencyApi(provider: NetworkProvider): CurrencyApi =
        provider.createCurrencyApi()

    @Provides
    @Singleton
    fun provideCurrencyNetworkDataSource(currencyApi: CurrencyApi): CurrencyNetworkDataSource {
        return CurrencyNetworkDataSourceImpl(currencyApi)
    }

}
