package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.domain.entity.Staff
import com.devjsg.cj_logistics_future_technology.presentation.home.admin.component.ShowDropdown

@Composable
fun ContestHomeScreen(navController: NavController) {
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

        // Filter & Sort
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
                    Button(onClick = { /* Handle sort by steps */ }) { Text("걸음수") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Handle sort by distance */ }) { Text("이동거리") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Handle sort by heart rate */ }) { Text("심박수") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("심박수 주의")
                Row {
                    Button(onClick = { /* Handle filter by heart rate warning */ }) { Text("필터 및 정렬 적용") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { /* Handle reset filters */ }) { Text("초기화") }
                }
            }
        }

        var listSize by remember { mutableStateOf(10) }
        ShowDropdown(listSize = listSize, onListSizeChange = { listSize = it })

        val staffList = listOf(
            Staff("John Doe", 80, 12000, 5.2f),
            Staff("Jane Smith", 75, 15000, 7.0f),
        )

        var currentPage by remember { mutableStateOf(1) }
        val totalPages = (staffList.size + 4) / 5
        val pageSize = 5
        val startPage = ((currentPage - 1) / pageSize) * pageSize + 1
        val endPage = minOf(startPage + pageSize - 1, totalPages)

        val paginatedList = staffList.drop((currentPage - 1) * 5).take(5)

        LazyColumn {
            items(paginatedList) { staff ->
                Row(modifier = Modifier.padding(16.dp)) {
                    Text(staff.name, modifier = Modifier.weight(1f))
                    Text("${staff.heartRate} bpm", modifier = Modifier.weight(1f))
                    Text("${staff.steps} steps", modifier = Modifier.weight(1f))
                    Text("${staff.distance} km", modifier = Modifier.weight(1f))
                }
                Divider()
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { currentPage = 1 }) {
                Text("<<")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { if (currentPage > 1) currentPage-- }) {
                Text("<")
            }

            Spacer(modifier = Modifier.width(8.dp))

            for (page in startPage..endPage) {
                Button(
                    onClick = { currentPage = page },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (page == currentPage) Color.Gray else Color.LightGray
                    )
                ) {
                    Text(page.toString())
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            Button(onClick = { if (currentPage < totalPages) currentPage++ }) {
                Text(">")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { currentPage = totalPages }) {
                Text(">>")
            }
        }
    }
}