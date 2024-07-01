package com.devjsg.cj_logistics_future_technology.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.di.util.decodeJwt
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    var isLoading by remember { mutableStateOf(true) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = null)

    LaunchedEffect(isLoggedIn) {
        delay(1000)
        if (isLoggedIn != null) {
            val decodedSub = decodeJwt(isLoggedIn!!)
            if (decodedSub == "1") {
                navController.navigate("admin_home") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("worker_home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            isLoading = false
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}