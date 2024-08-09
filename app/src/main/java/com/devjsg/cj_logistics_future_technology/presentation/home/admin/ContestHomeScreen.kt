package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    var listSize by remember { mutableStateOf(10) } // 기본 리스트 크기를 10으로 설정

    Column {
        Text(
            text = "Staff List",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Search Bar
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
                    Button(onClick = { viewModel.sorting.value = "MOVE_DESC"; viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize) }) { Text("걸음수") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.sorting.value = "KM_DESC"; viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize) }) { Text("이동거리") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.sorting.value = "HEART_RATE_DESC"; viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize) }) { Text("심박수") }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clickable {
                                warningText = when (warningText) {
                                    "심박수 주의" -> "걸음수 주의"
                                    "걸음수 주의" -> "이동거리 주의"
                                    else -> "심박수 주의"
                                }
                            }
                            .padding(vertical = 8.dp)
                    )
                    Button(onClick = { viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize) }) { Text("필터 및 정렬 적용") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.sorting.value = "MOVE_DESC"; viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize) }) { Text("초기화") }
                }
            }
        }

        ShowDropdown(listSize = listSize, onListSizeChange = { newSize ->
            listSize = newSize
            viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
        })

        val staffList by viewModel.staffList.collectAsState()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(staffList) { staff ->
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(staff.memberName, modifier = Modifier.weight(1f))
                    Text("${staff.heartRate} bpm", modifier = Modifier.weight(1f))
                    Text("${staff.moveWork} steps", modifier = Modifier.weight(1f))
                    Text("${staff.km} km", modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }

        PaginationRow(
            viewModel = viewModel,
            totalPages = 20,
            listSize = listSize
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
    }
}