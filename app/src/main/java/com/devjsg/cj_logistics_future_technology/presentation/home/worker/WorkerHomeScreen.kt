package com.devjsg.cj_logistics_future_technology.presentation.home.worker

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel

@Composable
fun WorkerHomeScreen(
    navController: NavController,
    viewModel: WorkerHomeViewModel = hiltViewModel()
) {
    val heartRate by viewModel.heartRateAvg.collectAsState()

    Log.d(TAG, "WorkerHomeScreen: $heartRate")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "근로자")
        Text(text = "Heart Rate: $heartRate BPM")

        Button(onClick = {
            viewModel.logout {
                navController.navigate("login") {
                    popUpTo("worker_home") { inclusive = true }
                }
            }
        }) {
            Text(text = "로그아웃")
        }
    }
}