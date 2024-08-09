package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowDropdown(listSize: Int, onListSizeChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(5, 10, 20)
    var selectedItem by remember { mutableStateOf(listSize) }

    Box(modifier = Modifier.padding(16.dp)) {
        TextButton(onClick = { expanded = true }) {
            Text("Show: $selectedItem")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { size ->
                DropdownMenuItem(
                    text = { Text("$size") },
                    onClick = {
                        selectedItem = size
                        onListSizeChange(size)
                        expanded = false
                    }
                )
            }
        }
    }
}