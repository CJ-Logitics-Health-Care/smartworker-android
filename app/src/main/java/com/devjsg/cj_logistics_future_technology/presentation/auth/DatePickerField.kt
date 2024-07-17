package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.R

@Composable
fun DatePickerIcon(
    date: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected("$selectedYear-${selectedMonth + 1}-$selectedDay")
            showDialog = false
        }, year, month, day).show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(width = 1.dp, Color.Gray, shape = RoundedCornerShape(size = 4.dp))
            .width(200.dp)
            .height(53.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 8.dp))
            .clickable { showDialog = true }
            .padding(start = 16.dp, end = 8.dp)
    ) {
        Text(
            text = date.ifEmpty { "생년월일" },
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
            contentDescription = "달력 아이콘",
            tint = Color.Black
        )
    }
}