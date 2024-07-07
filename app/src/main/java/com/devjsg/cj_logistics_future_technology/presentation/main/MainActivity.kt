package com.devjsg.cj_logistics_future_technology.presentation.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.home.DataSyncService
import com.devjsg.cj_logistics_future_technology.presentation.navigation.Navigation
import com.devjsg.cj_logistics_future_technology.presentation.service.HeartRateListenerService
import com.devjsg.cj_logistics_future_technology.presentation.theme.CJLogisticsFutureTechnologyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var biometricPromptHelper: BiometricPromptHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CJLogisticsFutureTechnologyTheme {
                val navController = rememberNavController()
                Navigation(navController = navController, biometricPromptHelper)
            }
        }

        val intent = Intent(this, HeartRateListenerService::class.java)
        startService(intent)

        startDataSyncService()
    }

    private fun startDataSyncService() {
        val intent = Intent(this, DataSyncService::class.java)
        ContextCompat.startForegroundService(this, intent)
        Log.d(TAG, "DataSyncService started")
    }
}