package com.ashna.pasa.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ashna.pasa.data.local.PasaPreferences
import com.ashna.pasa.features.dashboard.DashboardScreen
import com.ashna.pasa.features.security.SecurityStatusScreen
import com.ashna.pasa.features.setup.SetupScreen

@Composable
fun PasaNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferences = remember { PasaPreferences(context) }
    val isSetupComplete = preferences.isSetupComplete()

    val startDestination = if (isSetupComplete) "dashboard" else "setup"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("setup") {
            SetupScreen(
                onSetupComplete = {
                    navController.navigate("dashboard") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNavigateToSecurity = {
                    navController.navigate("security")
                }
            )
        }

        composable("security") {
            SecurityStatusScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
