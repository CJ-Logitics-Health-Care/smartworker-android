package com.devjsg.cj_logistics_future_technology.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.home.DataSyncService
import com.devjsg.cj_logistics_future_technology.presentation.navigation.Navigation
import com.devjsg.cj_logistics_future_technology.presentation.service.HeartRateListenerService
import com.devjsg.cj_logistics_future_technology.presentation.theme.CJLogisticsFutureTechnologyTheme
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var biometricPromptHelper: BiometricPromptHelper

    private val heartRateViewModel: WorkerHomeViewModel by viewModels()

    private val heartRateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == HeartRateListenerService.ACTION_HEART_RATE_AVG_UPDATE) {
                val heartRateAvg = intent.getIntExtra(HeartRateListenerService.EXTRA_HEART_RATE_AVG, 0)
                heartRateViewModel.setHeartRateAvg(heartRateAvg)
            }
        }
    }

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

        startDataSyncService()
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(heartRateReceiver, IntentFilter(HeartRateListenerService.ACTION_HEART_RATE_AVG_UPDATE),
                RECEIVER_NOT_EXPORTED)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(heartRateReceiver)
    }

    private fun startDataSyncService() {
        val intent = Intent(this, DataSyncService::class.java)
        ContextCompat.startForegroundService(this, intent)
        Log.d(TAG, "DataSyncService started")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}