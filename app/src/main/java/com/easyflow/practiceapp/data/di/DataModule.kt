package com.easyflow.practiceapp.data.di

import com.easyflow.practiceapp.data.data_api.CurrencyRepository
import com.easyflow.practiceapp.data.data_impl.CurrencyRepositoryImpl
import com.easyflow.practiceapp.network.network_api.data_sources.CurrencyNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyNetworkDataSource: CurrencyNetworkDataSource): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyNetworkDataSource)
    }

}
