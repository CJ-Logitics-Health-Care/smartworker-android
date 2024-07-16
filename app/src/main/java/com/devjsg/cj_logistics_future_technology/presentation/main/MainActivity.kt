package com.devjsg.cj_logistics_future_technology.presentation.main

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
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

                HandleIntent(navController)
            }
        }

        val intent = Intent(this, HeartRateListenerService::class.java)
        startService(intent)

        requestLocationPermission()

        heartRateReceiver = HeartRateBroadcastReceiver()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "알림 권한이 거부되었습니다. \n설정 화면에서 권한을 허용해 주세요.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun requestLocationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                Toast.makeText(this, "위치 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissionLauncher.launch(permissions)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        when {
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // 권한 설명이 필요한 경우
                Toast.makeText(this, "이 앱은 알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }

            else -> {
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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

    @Composable
    fun HandleIntent(navController: NavHostController) {
        val context = LocalContext.current as MainActivity
        val currentIntent by rememberUpdatedState(newValue = context.intent)

        LaunchedEffect(currentIntent) {
            currentIntent?.getStringExtra("navigateTo")?.let { navigateTo ->
                if (navigateTo == "maps") {
                    val latitude = currentIntent.getFloatExtra("latitude", 0f)
                    val longitude = currentIntent.getFloatExtra("longitude", 0f)
                    navController.navigate("maps/$latitude/$longitude")
                }
            }
        }
    }
}