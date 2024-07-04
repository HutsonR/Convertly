package com.easyflow.practiceapp.ui

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Live : Screen("liveCurrencies")
}