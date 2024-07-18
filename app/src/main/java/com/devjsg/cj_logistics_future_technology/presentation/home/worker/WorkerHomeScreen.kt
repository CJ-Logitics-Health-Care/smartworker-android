package com.devjsg.cj_logistics_future_technology.presentation.home.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(
    navController: NavController,
    viewModel: WorkerHomeViewModel = hiltViewModel()
) {
    val heartRate by viewModel.heartRateAvg.collectAsState()
    val myEmergencyReports by viewModel.myEmergencyReports.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "메인 화면",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF242424)
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout {
                            navController.navigate("login") {
                                popUpTo("worker_home") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "로그아웃",
                            modifier = Modifier.padding(end = 16.dp),
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF7F7F7)
                )
            )
        },
        containerColor = Color(0xFFF7F7F7),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "평균 심박수",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF242424)
                    )
                )
                Text(
                    text = "$heartRate BPM",
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = TextStyle(
                        fontSize = 60.sp,
                        lineHeight = 72.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF242424),
                    )
                )

                Text(
                    text = "신고 이력 조회",
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF242424),
                    )
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    items(myEmergencyReports) { report ->
                        MyEmergencyReportItem(report)
                    }
                }
            }
        }
    )
}