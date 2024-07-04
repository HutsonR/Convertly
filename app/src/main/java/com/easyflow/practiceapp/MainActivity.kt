package com.easyflow.practiceapp

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easyflow.practiceapp.ui.Screen
import com.easyflow.practiceapp.ui.home.HomeScreen
import com.easyflow.practiceapp.ui.live.LiveCurrenciesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(Screen.Live.route) {
                    LiveCurrenciesScreen(navController = navController)
                }
            }
            SetStatusBarColor(window)
        }
    }

    @Composable
    private fun SetStatusBarColor(window: Window) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.statusBarColor = colorResource(id = R.color.currency_change_background).toArgb()
        windowInsetsController.isAppearanceLightStatusBars = false
    }
}