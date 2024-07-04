package com.easyflow.practiceapp.domain.di

import com.easyflow.practiceapp.data.data_api.CurrencyRepository
import com.easyflow.practiceapp.domain.domain_api.CurrencyUseCase
import com.easyflow.practiceapp.domain.domain_impl.CurrencyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideCurrencyUseCase(currencyRepository: CurrencyRepository): CurrencyUseCase {
        return CurrencyUseCaseImpl(currencyRepository)
    }

}
