package com.devjsg.watch.presentation.home

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
import com.devjsg.watch.presentation.viewmodel.HeartRateViewModel

@Composable
fun HeartRateScreen(viewModel: HeartRateViewModel = hiltViewModel()) {
    val heartRate by viewModel.heartRate.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Heart Rate: $heartRate BPM")
        Button(onClick = { viewModel.startMonitoring() }) {
            Text("Start Monitoring")
        }
        Button(onClick = { viewModel.stopMonitoring() }) {
            Text("Stop Monitoring")
        }
    }
}