package com.easyflow.practiceapp.network.network_impl.mappers

import com.easyflow.practiceapp.domain.models.CurrencyModel
import com.easyflow.practiceapp.domain.models.ValuteModel
import com.easyflow.practiceapp.network.network_impl.models.CurrencyApiModel
import com.easyflow.practiceapp.network.network_impl.models.ValuteApiModel

fun CurrencyApiModel.mapToCurrencyModel(): CurrencyModel =
    CurrencyModel(
        date = this.date ?: "",
        previousDate = this.previousDate ?: "",
        previousURL = this.previousURL ?: "",
        timestamp = this.timestamp ?: "",
        valute = this.valute?.mapValues { mapToValuteModel(it.value) } ?: emptyMap()
    )

private fun mapToValuteModel(apiModel: ValuteApiModel): ValuteModel =
    ValuteModel(
        charCode = apiModel.charCode,
        nominal = apiModel.nominal,
        name = apiModel.name,
        value = apiModel.value,
        previous = apiModel.previous
    )