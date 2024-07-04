package com.easyflow.practiceapp.ui.live

import androidx.lifecycle.viewModelScope
import com.easyflow.practiceapp.core.BaseViewModel
import com.easyflow.practiceapp.domain.domain_api.CurrencyUseCase
import com.easyflow.practiceapp.domain.models.ValuteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveCurrenciesViewModel @Inject constructor(
    private val currencyUseCase: CurrencyUseCase
) : BaseViewModel<LiveCurrenciesViewModel.State, LiveCurrenciesViewModel.Actions>(State()) {

    init {
        viewModelScope.launch {
            modifyState { copy(isLoading = true) }
            try {
                val list = currencyUseCase.getCurrencyRates()
                val valuteList = list.valute.values.toList()
                modifyState {
                    copy(
                        dataList = valuteList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onAction(Actions.ShowToast)
            } finally {
                modifyState { copy(isLoading = false) }
            }
        }
    }

    fun goBack() {
        onAction(Actions.GoBack)
    }

    data class State(
        val dataList: List<ValuteModel> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed interface Actions {
        data object GoBack : Actions
        data object ShowToast : Actions
    }
}