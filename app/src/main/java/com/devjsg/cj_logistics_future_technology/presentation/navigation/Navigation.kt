package com.devjsg.cj_logistics_future_technology.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.auth.SignUpScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.AdminHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.WorkerHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.login.LoginScreen
import com.devjsg.cj_logistics_future_technology.presentation.map.MapsScreen
import com.devjsg.cj_logistics_future_technology.presentation.splash.SplashScreen
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    biometricPromptHelper: BiometricPromptHelper,
    heartRateViewModel: WorkerHomeViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("sign_up") { SignUpScreen(navController = navController) }
        composable("login") {
            LoginScreen(
                navController = navController,
                biometricPromptHelper = biometricPromptHelper
            )
        }
        composable("admin_home") { AdminHomeScreen(navController, heartRateViewModel) }
        composable("worker_home") { WorkerHomeScreen(navController = navController) }
        composable(
            route = "maps/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble() ?: 0.0
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble() ?: 0.0
            Log.d("MapsScreen", "Latitude: $latitude, Longitude: $longitude")
            MapsScreen(latitude = latitude, longitude = longitude)
        }
    }
}