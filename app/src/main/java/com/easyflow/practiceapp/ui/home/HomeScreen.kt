package com.easyflow.practiceapp.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowRightAlt
import androidx.compose.material.icons.rounded.Cached
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.easyflow.practiceapp.R
import com.easyflow.practiceapp.domain.models.ValuteModel
import com.easyflow.practiceapp.ui.Screen
import com.easyflow.practiceapp.ui.home.utils.CurrencyDirection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    ObserveActions(viewModel, navController)

    Surface(
        modifier = Modifier
            .background(colorResource(id = R.color.main_background))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                CurrencyConverterScreen(
                    state,
                    onFirstCurrencySelected = { currency ->
                        viewModel.firstCurrencyChanged(currency)
                    },
                    onSecondCurrencySelected = { currency ->
                        viewModel.secondCurrencyChanged(currency)
                    },
                    onCurrencyCountEntered = { count ->
                        viewModel.currencyCountChanged(
                            if (count.isEmpty()) {
                                1
                            } else {
                                count.toInt()
                            }
                        )
                    }
                )
                ToLiveCurrenciesButton(viewModel)
                PickedCurrencyValues(state)
                Spacer(modifier = Modifier.weight(1f))
                CurrencyConvertText(state.convertAmount.toString())
                Spacer(modifier = Modifier.weight(1f))
                CurrencyConverterButton(onClick = { viewModel.onCurrencyConvertButtonClick() })
            }
        }
    }
}

@Composable
private fun Header(date: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(52.dp),
            imageVector = Icons.Rounded.CurrencyExchange,
            contentDescription = null,
            tint = Color.White,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = date,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = stringResource(id = R.string.currency_change_fresh_rates),
                color = Color.Green,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun CurrencyConverterScreen(
    state: HomeViewModel.State,
    onFirstCurrencySelected: (String) -> Unit,
    onSecondCurrencySelected: (String) -> Unit,
    onCurrencyCountEntered: (String) -> Unit
) {
    val date = remember { SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(Date()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.currency_change_background))
            .padding(20.dp)
    ) {
        Header(date = date)
        Spacer(modifier = Modifier.height(16.dp))
        CurrencySelector(
            state = state,
            onFirstCurrencySelected = { currency ->
                onFirstCurrencySelected(currency)
            },
            onSecondCurrencySelected = { currency ->
                onSecondCurrencySelected(currency)
            },
            onCurrencyCountEntered = { count ->
                onCurrencyCountEntered(count)
            }
        )
    }
}

@Composable
private fun CurrencySelector(
    state: HomeViewModel.State,
    onFirstCurrencySelected: (String) -> Unit,
    onSecondCurrencySelected: (String) -> Unit,
    onCurrencyCountEntered: (String) -> Unit
) {
    var showChooseCurrencyDialog by remember { mutableStateOf(false) }
    var currentDirectionChoosen: CurrencyDirection by remember { mutableStateOf(CurrencyDirection.FROM) }
    val allCurrencies = state.dataList
    val firstCurrencyCharCode = state.firstCurrency?.charCode ?: ""
    val secondCurrencyCharCode = state.secondCurrency?.charCode ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CurrencyButton(
                currencyDirection = CurrencyDirection.FROM,
                currency = firstCurrencyCharCode
            ) {
                currentDirectionChoosen = CurrencyDirection.FROM
                showChooseCurrencyDialog = true
            }
        }

        Icon(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 32.dp)
                .size(32.dp),
            imageVector = Icons.Rounded.Cached,
            contentDescription = null,
            tint = colorResource(id = R.color.currency_change_text),
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            CurrencyButton(
                currencyDirection = CurrencyDirection.TO,
                currency = secondCurrencyCharCode
            ) {
                currentDirectionChoosen = CurrencyDirection.TO
                showChooseCurrencyDialog = true
            }
        }
    }

    CurrencyCountEnter(state.currencyCount.toString()) { count ->
        onCurrencyCountEntered(count)
    }

    if (showChooseCurrencyDialog) {
        CurrencyDialog(
            allCurrencies = allCurrencies,
            onDismiss = { showChooseCurrencyDialog = false },
            onCurrencySelected = { selectedCurrency ->
                when (currentDirectionChoosen) {
                    CurrencyDirection.FROM -> onFirstCurrencySelected(selectedCurrency)
                    CurrencyDirection.TO -> onSecondCurrencySelected(selectedCurrency)
                }
                showChooseCurrencyDialog = false
            }
        )
    }
}

@Composable
private fun CurrencyButton(
    currencyDirection: CurrencyDirection,
    currency: String,
    onClick: () -> Unit
) {
    val directionText: String = when (currencyDirection) {
        CurrencyDirection.FROM -> stringResource(id = R.string.home_currency_direction_from)
        CurrencyDirection.TO -> stringResource(id = R.string.home_currency_direction_to)
    }
    Column {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = directionText,
            fontSize = 14.sp,
            color = colorResource(id = R.color.currency_change_text),
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.currency_change_item_background)),
            shape = MaterialTheme.shapes.small.copy()
        ) {
            Text(
                color = colorResource(id = R.color.currency_change_text),
                text = currency,
                fontSize = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CurrencyCountEnter(
    count: String,
    onClick: (String) -> Unit
) {
    val pattern = remember { Regex("^\\d+\$") }
    var currencyCount: String by remember { mutableStateOf(count) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                bottom = 16.dp,
                start = 16.dp
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(id = R.string.home_currency_count),
            fontSize = 14.sp,
            color = colorResource(id = R.color.currency_change_text),
        )
        TextField(
            value = currencyCount,
            onValueChange = {
                if (it.isEmpty() || it.matches(pattern)) {
                    currencyCount = it
                    onClick(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = colorResource(id = R.color.currency_change_item_background),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = colorResource(id = R.color.white),
                unfocusedTextColor = colorResource(id = R.color.white)
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            shape = MaterialTheme.shapes.small.copy()
        )
    }
}

@Composable
private fun CurrencyDialog(
    allCurrencies: List<ValuteModel>,
    onDismiss: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("") }

    val filteredCurrencies = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            allCurrencies
        } else {
            allCurrencies.filter { it.charCode.contains(searchQuery, ignoreCase = true) }
        }
    }

    AlertDialog(
        modifier = Modifier
            .padding(vertical = 32.dp),
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.home_dialog_choose_valute)) },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(id = R.string.search)) }
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    items(filteredCurrencies) { currency ->
                        RadioButton(
                            currency = currency,
                            selectedCurrency = selectedCurrency,
                            onItemClick = {
                                selectedCurrency = it.charCode
                                Log.d("debugTag", "selectedCurrency $selectedCurrency")
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onCurrencySelected(selectedCurrency)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
private fun RadioButton(
    currency: ValuteModel,
    selectedCurrency: String,
    onItemClick: (ValuteModel) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedCurrency == currency.charCode,
            onClick = { onItemClick(currency) },
            enabled = true,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
        )
        Text(text = currency.charCode, modifier = Modifier.padding(start = 8.dp), color = colorResource(id = R.color.black), fontWeight = Bold)
        Text(text = currency.name, modifier = Modifier.padding(start = 6.dp), color = colorResource(id = R.color.textGray))
    }
}


@Composable
private fun ToLiveCurrenciesButton(viewModel: HomeViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(48.dp),
        onClick = { viewModel.goToLiveCurrencies() },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.currency_change_item_background)),
        shape = MaterialTheme.shapes.small.copy()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                color = colorResource(id = R.color.currency_change_text),
                text = stringResource(id = R.string.currency_live_button),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(32.dp),
                imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                contentDescription = null,
                tint = colorResource(id = R.color.currency_change_text),
            )
        }
    }
}

@Composable
private fun PickedCurrencyValues(state: HomeViewModel.State) {
    val firstCurrency = state.firstCurrency
    val secondCurrency = state.secondCurrency

    Column(
        modifier = Modifier.padding(vertical = 0.dp, horizontal = 12.dp)
    ) {
        if (firstCurrency != null) {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp),
                text = "${firstCurrency.nominal} ${firstCurrency.name} = ${firstCurrency.value}",
                color = colorResource(id = R.color.textGray),
                fontSize = 16.sp
            )
        }
        if (secondCurrency != null) {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp),
                text = "${secondCurrency.nominal} ${secondCurrency.name} = ${secondCurrency.value}",
                color = colorResource(id = R.color.textGray),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun CurrencyConvertText(amount: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(26.dp),
        text = amount,
        color = colorResource(id = R.color.black),
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CurrencyConverterButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(26.dp)
            .height(54.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.currency_change_item_background)),
    ) {
        Text(
            color = colorResource(id = R.color.currency_change_text),
            text = stringResource(id = R.string.currency_change_button),
            fontSize = 16.sp,
            fontWeight = Bold
        )
    }
}

@Composable
private fun ObserveActions(viewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
    val errorMessage = stringResource(id = R.string.error)

    LaunchedEffect(key1 = context) {
        viewModel.action.collect { action ->
            when (action) {
                is HomeViewModel.Actions.GoToLiveCurrencies -> {
                    navController.navigate(Screen.Live.route)
                }

                is HomeViewModel.Actions.ShowToast -> {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}