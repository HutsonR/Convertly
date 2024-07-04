package com.easyflow.practiceapp.ui.home

import androidx.lifecycle.viewModelScope
import com.easyflow.practiceapp.core.BaseViewModel
import com.easyflow.practiceapp.domain.domain_api.CurrencyUseCase
import com.easyflow.practiceapp.domain.models.ValuteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyUseCase: CurrencyUseCase
) : BaseViewModel<HomeViewModel.State, HomeViewModel.Actions>(State()) {

    init {
        viewModelScope.launch {
            try {
                val list = currencyUseCase.getCurrencyRates()
                val valuteList = list.valute.values.toList()
                modifyState {
                    copy(
                        dataList = valuteList
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onAction(Actions.ShowToast)
            }
        }
    }

    fun onCurrencyConvertButtonClick() {
        viewModelScope.launch {
            val firstCurrency = state.value.firstCurrency
            val secondCurrency = state.value.secondCurrency
            val currencyCount = state.value.currencyCount
            if (firstCurrency != null && secondCurrency != null) {

                val convertedAmount = (firstCurrency.value / firstCurrency.nominal) * currencyCount * (secondCurrency.nominal / secondCurrency.value)

                val formattedAmount = String.format(Locale.US, "%.5f", convertedAmount).replace(',', '.')

                modifyState {
                    copy(
                        convertAmount = formattedAmount.toDouble(),
                        firstCurrency = firstCurrency,
                        secondCurrency = secondCurrency
                    )
                }
            }
        }
    }

    fun firstCurrencyChanged(currencyCharCode: String) {
        viewModelScope.launch {
            val currency = state.value.dataList.find { it.charCode == currencyCharCode }
            modifyState {
                copy(
                    firstCurrency = currency
                )
            }
        }
    }

    fun secondCurrencyChanged(currencyCharCode: String) {
        viewModelScope.launch {
            val currency = state.value.dataList.find { it.charCode == currencyCharCode }
            modifyState {
                copy(
                    secondCurrency = currency
                )
            }
        }
    }

    fun currencyCountChanged(count: Int) {
        viewModelScope.launch {
            modifyState { copy(currencyCount = count) }
        }
    }

    fun goToLiveCurrencies() =
        onAction(Actions.GoToLiveCurrencies)

    data class State(
        val convertAmount: Double = 0.0,
        val dataList: List<ValuteModel> = emptyList(),
        val firstCurrency: ValuteModel? = null,
        val secondCurrency: ValuteModel? = null,
        val currencyCount: Int = 1
    )

    sealed interface Actions {
        data object GoToLiveCurrencies : Actions
        data object ShowToast : Actions
    }
}