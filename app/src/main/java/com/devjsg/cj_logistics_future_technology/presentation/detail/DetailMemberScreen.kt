package com.devjsg.cj_logistics_future_technology.presentation.detail

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.presentation.detail.component.HeartRateChart
import com.devjsg.cj_logistics_future_technology.presentation.detail.component.MemberEmergencyReportItem
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberDetailViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMemberScreen(
    navController: NavController,
    memberId: Int,
    viewModel: MemberDetailViewModel = hiltViewModel()
) {
    val heartRateData by viewModel.heartRateData.collectAsState()
    var selectedOption by remember { mutableStateOf("전체") }

    val options = listOf("전체", "하루", "7일", "30일", "60일", "90일")
    val context = LocalContext.current

    LaunchedEffect(selectedOption) {
        val (start, end) = getDateTimeForOption(selectedOption)
        viewModel.getHeartRateData(memberId, start, end)
    }

    LaunchedEffect(selectedOption) {
        val (start, end) = getDateForOption(selectedOption)
        viewModel.loadEmergencyReports(memberId, start, end)
    }

    val memberEmergencyReports by viewModel.memberEmergencyReports.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("회원 상세 정보") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(options.size) { index ->
                        val option = options[index]
                        Button(onClick = { selectedOption = option }) {
                            Text(option)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp)
                        .padding(13.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(16.dp))
                ) {
                    HeartRateChart(heartRateData)
                }

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
                    items(memberEmergencyReports) { report ->
                        MemberEmergencyReportItem(report)
                    }
                }
            }
        }
    )
}

private fun getDateForOption(option: String): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val endDate = Date()
    val startDate = Calendar.getInstance()

    when (option) {
        "하루" -> startDate.add(Calendar.DAY_OF_YEAR, -1)
        "7일" -> startDate.add(Calendar.DAY_OF_YEAR, -7)
        "30일" -> startDate.add(Calendar.DAY_OF_YEAR, -30)
        "60일" -> startDate.add(Calendar.DAY_OF_YEAR, -60)
        "90일" -> startDate.add(Calendar.DAY_OF_YEAR, -90)
        "전체" -> {
            startDate.set(1900, Calendar.JANUARY, 1)
        }
    }

    return dateFormat.format(startDate.time) to dateFormat.format(endDate)
}
private fun getDateTimeForOption(option: String): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:00:ss", Locale.getDefault())
    val endDate = Date()
    val startDate = Calendar.getInstance()

    when (option) {
        "하루" -> startDate.add(Calendar.DAY_OF_YEAR, -1)
        "7일" -> startDate.add(Calendar.DAY_OF_YEAR, -7)
        "30일" -> startDate.add(Calendar.DAY_OF_YEAR, -30)
        "60일" -> startDate.add(Calendar.DAY_OF_YEAR, -60)
        "90일" -> startDate.add(Calendar.DAY_OF_YEAR, -90)
        "전체" -> {
            startDate.set(1900, Calendar.JANUARY, 1)
        }
    }

    return dateFormat.format(startDate.time) to dateFormat.format(endDate)
}