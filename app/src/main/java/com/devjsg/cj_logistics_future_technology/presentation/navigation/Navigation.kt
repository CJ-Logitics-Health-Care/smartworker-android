package com.devjsg.cj_logistics_future_technology.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devjsg.cj_logistics_future_technology.presentation.auth.SignUpScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.adminHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.workerHomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.login.LoginScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("sign_up") { SignUpScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("admin_home") { adminHomeScreen(navController = navController) }
        composable("worker_home") { workerHomeScreen(navController = navController) }
    }
}