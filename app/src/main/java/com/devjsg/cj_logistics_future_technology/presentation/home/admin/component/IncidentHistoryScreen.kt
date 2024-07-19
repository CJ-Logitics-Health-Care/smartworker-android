package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentHistoryScreen(viewModel: AdminViewModel) {
    var loginId by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val context = LocalContext.current

    val startDatePickerDialog = remember {
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            startDate = "$year-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
        }, 2023, 6, 19)
    }

    val endDatePickerDialog = remember {
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            endDate = "$year-${String.format("%02d", month + 1)}-${String.format("%02d", dayOfMonth)}"
        }, 2023, 6, 19)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = loginId,
            onValueChange = { loginId = it },
            label = { Text("회원 아이디로 검색") },
            trailingIcon = {
                IconButton(onClick = {
                    // 검색 실행 로직
                    when {
                        loginId.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.searchEmergencyReports(loginId, startDate, endDate)
                        }
                        loginId.isEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.getEmergencyReportsWithDate(startDate, endDate)
                        }
                        loginId.isNotEmpty() && startDate.isEmpty() && endDate.isEmpty() -> {
                            viewModel.searchEmergencyReports(loginId)
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // 검색 실행 로직
                    when {
                        loginId.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.searchEmergencyReports(loginId, startDate, endDate)
                        }
                        loginId.isEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() -> {
                            viewModel.getEmergencyReportsWithDate(startDate, endDate)
                        }
                        loginId.isNotEmpty() && startDate.isEmpty() && endDate.isEmpty() -> {
                            viewModel.searchEmergencyReports(loginId)
                        }
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = { Text("시작 날짜") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { startDatePickerDialog.show() }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Start Date")
                    }
                },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = { Text("종료 날짜") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { endDatePickerDialog.show() }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "End Date")
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 신고 이력 리스트
        val emergencyReports by viewModel.emergencyReports.collectAsState()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            emergencyReports?.let { reports ->
                items(reports) { report ->
                    EmergencyReportItem(report)
                }
            }
        }
    }
}