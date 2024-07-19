package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.presentation.auth.DatePickerIcon

@Composable
fun EditMemberBottomSheet(
    member: EditableMember,
    onDismiss: () -> Unit,
    onSave: (EditableMember) -> Unit
) {
    var name by remember { mutableStateOf(member.employeeName) }
    var phone by remember { mutableStateOf(member.phone) }
    var gender by remember { mutableStateOf(member.gender) }
    var email by remember { mutableStateOf(member.email) }
    var authority by remember { mutableStateOf(member.authority) }
    var heartRateThreshold by remember { mutableStateOf(member.heartRateThreshold.toString()) }

    var date by remember {
        mutableStateOf("${member.year}-${member.month}-${member.day}")
    }

    var expanded by remember { mutableStateOf(false) }
    val authorityOptions = listOf("ADMIN", "EMPLOYEE")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
    ) {
        Text(text = "프로필 편집", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Date of Birth")
        DatePickerIcon(
            date = date,
            onDateSelected = { selectedDate ->
                date = selectedDate
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = heartRateThreshold,
            onValueChange = { heartRateThreshold = it },
            label = { Text("heartRateThreshold") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Authority")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Text(text = authority)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                authorityOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            authority = option
                            expanded = false
                        },
                        text = { Text(option) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(62.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFDFDFDF),
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(size = 8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    "취소",
                    color = Color(0xFF242424),
                    style = TextStyle(
                        fontWeight = FontWeight(700)
                    )
                )
            }
            Button(
                onClick = {
                    val (selectedYear, selectedMonth, selectedDay) = date.split("-")
                        .map { it.toInt() }
                    val updatedMember = member.copy(
                        employeeName = name,
                        phone = phone,
                        gender = gender,
                        email = email,
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDay,
                        authority = authority,
                        heartRateThreshold = heartRateThreshold.toInt()
                    )
                    onSave(updatedMember)
                },
                modifier = Modifier
                    .weight(3f)
                    .height(62.dp)
                    .background(color = Color(0xFFEF151E), shape = RoundedCornerShape(size = 8.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF151E))
            ) {
                Text(
                    "저장하기",
                    style = TextStyle(
                        fontWeight = FontWeight(700)
                    )
                )
            }
        }
    }
}