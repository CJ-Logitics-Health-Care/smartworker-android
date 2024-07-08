package com.devjsg.cj_logistics_future_technology.presentation.main

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.data.source.remote.HeartRateListenerService
import com.devjsg.cj_logistics_future_technology.presentation.navigation.Navigation
import com.devjsg.cj_logistics_future_technology.presentation.theme.CJLogisticsFutureTechnologyTheme
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var biometricPromptHelper: BiometricPromptHelper

    private val heartRateViewModel: WorkerHomeViewModel by viewModels()

    private lateinit var heartRateReceiver: HeartRateBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CJLogisticsFutureTechnologyTheme {
                val navController = rememberNavController()
                Navigation(navController = navController, biometricPromptHelper, heartRateViewModel)
            }
        }

        val intent = Intent(this, HeartRateListenerService::class.java)
        startService(intent)

        heartRateReceiver = HeartRateBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(HeartRateListenerService.ACTION_HEART_RATE_AVG_UPDATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(heartRateReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(heartRateReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(heartRateReceiver)
    }
}