package com.devjsg.cj_logistics_future_technology.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.auth.SignUpScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.AdminHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.WorkerHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.login.LoginScreen
import com.devjsg.cj_logistics_future_technology.presentation.splash.SplashScreen

@Composable
fun Navigation(navController: NavHostController, biometricPromptHelper: BiometricPromptHelper) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("sign_up") { SignUpScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController, biometricPromptHelper = biometricPromptHelper) }
        composable("admin_home") { AdminHomeScreen(navController = navController) }
        composable("worker_home") { WorkerHomeScreen(navController = navController) }
    }
}