package com.easyflow.practiceapp.domain.domain_impl

import com.easyflow.practiceapp.domain.domain_api.CurrencyUseCase
import com.easyflow.practiceapp.data.data_api.CurrencyRepository
import com.easyflow.practiceapp.domain.models.CurrencyModel
import javax.inject.Inject

class CurrencyUseCaseImpl @Inject constructor (
    private val currencyRepository: CurrencyRepository
): CurrencyUseCase {

    override suspend fun getCurrencyRates(): CurrencyModel =
        currencyRepository.getCurrencyRates()

}