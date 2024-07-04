package com.easyflow.practiceapp.ui.live

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.easyflow.practiceapp.R
import com.easyflow.practiceapp.domain.models.ValuteModel
import com.easyflow.practiceapp.ui.Screen
import com.easyflow.practiceapp.ui.home.HomeViewModel
import java.util.Locale
import kotlin.math.abs

@Composable
internal fun LiveCurrenciesScreen(
    navController: NavController,
    viewModel: LiveCurrenciesViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    ObserveActions(viewModel, navController)

    Column {
        Header(onClick = { viewModel.goBack() })
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.dataList) { currency ->
                        CurrencyItem(currency)
                    }
                }
            }
        }
    }

}

@Composable
private fun Header(onClick: () -> Unit) {
    Row (
        modifier = Modifier
            .padding(bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = colorResource(id = R.color.black),
            )
        }
        Text(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 12.dp),
            text = stringResource(id = R.string.currency_live_button),
            fontSize = 20.sp,
            fontWeight = Bold,
            color = colorResource(id = R.color.black)
        )
    }
}

@Composable
private fun CurrencyItem(currency: ValuteModel) {
    val isUp = currency.value > currency.previous
    val image = if (isUp) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward
    val color = if (isUp) R.color.green else R.color.red
    Row (
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = currency.name,
                fontSize = 16.sp,
                color = colorResource(id = R.color.black)
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = currency.charCode,
                fontSize = 14.sp,
                color = colorResource(id = R.color.textGray)
            )
        }
        Column {
            Text(
                text = currency.value.toString(),
                fontSize = 16.sp,
                color = colorResource(id = R.color.black)
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = calcPercentValuteChange(currency),
                    fontSize = 14.sp,
                    color = colorResource(id = color)
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(16.dp),
                    imageVector = image,
                    contentDescription = null,
                    tint = colorResource(id = color),
                )
            }
        }
    }
}

private fun calcPercentValuteChange(currency: ValuteModel): String {
    val difference = currency.value - currency.previous
    val sign = if (difference > 0) "+" else "-"
    val percent: Double = (difference / currency.previous) * 100

    return String.format(Locale.US, "%s%.2f%%", sign, abs(percent))
}


@Composable
private fun ObserveActions(viewModel: LiveCurrenciesViewModel, navController: NavController) {
    val context = LocalContext.current
    val errorMessage = stringResource(id = R.string.error)

    LaunchedEffect(key1 = context) {
        viewModel.action.collect { action ->
            when (action) {
                is LiveCurrenciesViewModel.Actions.GoBack -> {
                    navController.popBackStack()
                }
                is LiveCurrenciesViewModel.Actions.ShowToast -> {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}