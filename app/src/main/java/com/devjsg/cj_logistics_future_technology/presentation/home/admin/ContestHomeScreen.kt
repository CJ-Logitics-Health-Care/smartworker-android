package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.PaginationRow
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.ShowDropdown
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.ContestHomeViewModel

@Composable
fun ContestHomeScreen(
    navController: NavController,
    viewModel: ContestHomeViewModel = hiltViewModel()
) {
    var warningText by remember { mutableStateOf("심박수 주의") }
    var listSize by remember { mutableStateOf(10) }

    Column {
        Text(
            text = "Staff List",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        var searchQuery by remember { mutableStateOf("") }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("근무자 이름 검색") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )

        var showFilters by remember { mutableStateOf(false) }
        Button(
            onClick = { showFilters = !showFilters },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "필터 및 정렬")
        }

        if (showFilters) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("정렬 조건")
                Row {
                    Button(onClick = { viewModel.toggleMoveSortOrder() }) {
                        Text(
                            text = when (viewModel.moveSortOrder.value) {
                                "MOVE_DESC" -> "걸음수 내림차순"
                                "MOVE_ASC" -> "걸음수 오름차순"
                                else -> "걸음수"
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.toggleKmSortOrder() }) {
                        Text(
                            text = when (viewModel.kmSortOrder.value) {
                                "KM_DESC" -> "이동거리 내림차순"
                                "KM_ASC" -> "이동거리 오름차순"
                                else -> "이동거리"
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.toggleHeartRateSortOrder() }) {
                        Text(
                            text = when (viewModel.heartRateSortOrder.value) {
                                "HEART_RATE_DESC" -> "심박수 내림차순"
                                "HEART_RATE_ASC" -> "심박수 오름차순"
                                else -> "심박수"
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clickable {
                                warningText = when (warningText) {
                                    "심박수 주의" -> {
                                        viewModel.updateReportCondition("MOVE_FILTER")
                                        "걸음수 주의"
                                    }

                                    "걸음수 주의" -> {
                                        viewModel.updateReportCondition("KM_FILTER")
                                        "이동거리 주의"
                                    }

                                    else -> {
                                        viewModel.updateReportCondition("HEART_RATE_FILTER")
                                        "심박수 주의"
                                    }
                                }
                            }
                            .padding(vertical = 8.dp)
                    )
                    Button(onClick = {
                        viewModel.applySortingAndFiltering()
                    }) { Text("필터 및 정렬 적용") }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        viewModel.resetSortingAndFiltering()
                    }) { Text("초기화") }
                }
            }
        }

        ShowDropdown(listSize = listSize, onListSizeChange = { newSize ->
            listSize = newSize
            viewModel.updateListSize(newSize)
            viewModel.applySortingAndFiltering()
        })

        val staffList by viewModel.staffList.collectAsState()
        val totalPages = viewModel.totalPages.value

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(staffList) { staff ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    VerticalDivider(
                        color = if (staff.isOverHeartRate) Color.Red else Color.Transparent,
                        thickness = 4.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(staff.memberName, modifier = Modifier.weight(1f))
                    Text("${staff.heartRate} bpm", modifier = Modifier.weight(1f))
                    Text("${staff.moveWork} steps", modifier = Modifier.weight(1f))
                    Text("${staff.km} km", modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }

        Log.d("totalPages", totalPages.toString())
        PaginationRow(
            viewModel = viewModel,
            totalPages = totalPages
        )
    }

    LaunchedEffect(viewModel.currentPage.value, listSize) {
        viewModel.applySortingAndFiltering()
    }
}