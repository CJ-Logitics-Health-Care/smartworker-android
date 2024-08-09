package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.ContestHomeViewModel

@Composable
fun PaginationRow(
    viewModel: ContestHomeViewModel,
    totalPages: Int,
    listSize: Int
) {
    val pagesPerGroup = 5
    var currentGroupStart by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "<<",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    currentGroupStart = 1
                    viewModel.currentPage.value = 1
                    viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
                }
        )

        Text(
            text = "<",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    if (currentGroupStart > 1) {
                        currentGroupStart -= pagesPerGroup
                        viewModel.currentPage.value = currentGroupStart
                        viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
                    }
                }
        )

        for (page in currentGroupStart until currentGroupStart + pagesPerGroup) {
            if (page > totalPages) break
            Text(
                text = page.toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        viewModel.currentPage.value = page
                        viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
                    },
                color = if (page == viewModel.currentPage.value) Color.Gray else Color.Black
            )
        }

        Text(
            text = ">",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    if (currentGroupStart + pagesPerGroup <= totalPages) {
                        currentGroupStart += pagesPerGroup
                        viewModel.currentPage.value = currentGroupStart
                        viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
                    }
                }
        )

        Text(
            text = ">>",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable {
                    currentGroupStart = (totalPages / pagesPerGroup) * pagesPerGroup + 1
                    viewModel.currentPage.value = totalPages
                    viewModel.loadStaff(viewModel.currentPage.value, viewModel.sorting.value, listSize)
                }
        )
    }
}