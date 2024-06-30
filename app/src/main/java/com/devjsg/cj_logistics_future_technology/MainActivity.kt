package com.devjsg.cj_logistics_future_technology

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.devjsg.cj_logistics_future_technology.presentation.navigation.Navigation
import com.devjsg.cj_logistics_future_technology.ui.theme.CJLogisticsFutureTechnologyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CJLogisticsFutureTechnologyTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}