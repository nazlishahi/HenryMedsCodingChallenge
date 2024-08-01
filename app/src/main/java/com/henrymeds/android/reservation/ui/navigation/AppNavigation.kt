package com.henrymeds.android.reservation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.henrymeds.android.reservation.ui.screen.ClientScreen
import com.henrymeds.android.reservation.ui.screen.HomeScreen
import com.henrymeds.android.reservation.ui.screen.ProviderScreen
import com.henrymeds.android.reservation.viewmodel.MainActivityViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen(navController) }
        composable("provider") {
            ProviderScreen(viewModel)
        }
        composable("client") {
            ClientScreen(navController, viewModel)
        }
    }
}