package com.devjsg.cj_logistics_future_technology.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.HeartRateViewModel

@Composable
fun HeartRateScreen(viewModel: HeartRateViewModel = hiltViewModel()) {
    val heartRate by viewModel.heartRate.collectAsState()
    val heartRateAvg by viewModel.heartRateAvg.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Heart Rate: $heartRate BPM")

        Button(onClick = {
            viewModel.startService()
        }) {
            Text("Start Monitoring")
        }

        Button(onClick = {
            viewModel.stopService()
        }) {
            Text("Stop Monitoring")
        }

        Text("Heart Rate Avg: $heartRateAvg BPM")
    }
}