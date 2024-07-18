package com.devjsg.cj_logistics_future_technology.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberDetailViewModel

@Composable
fun DetailMemberScreen(
    memberId: Int,
    viewModel: MemberDetailViewModel = hiltViewModel()
) {
    val selectedOption = remember { mutableStateOf("어제") }
    val options = listOf("어제", "7일", "30일", "60일", "90일")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "회원 상세 조회",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(options) { option ->
                DateOptionItem(option = option, isSelected = option == selectedOption.value) {
                    selectedOption.value = option
                }
            }
        }

    }
}
