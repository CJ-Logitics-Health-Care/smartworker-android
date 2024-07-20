package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfo
import com.devjsg.cj_logistics_future_technology.presentation.auth.DatePickerIcon
import com.devjsg.cj_logistics_future_technology.presentation.auth.GenderOption

@Composable
fun EditMemberBottomSheet(
    member: MemberInfo,
    onDismiss: () -> Unit,
    onSave: (MemberInfo) -> Unit
) {
    var name by remember { mutableStateOf(member.employeeName) }
    var phone by remember { mutableStateOf(TextFieldValue(member.phone)) }
    var gender by remember { mutableStateOf(member.gender) }
    var email by remember { mutableStateOf(member.email) }
    var authority by remember { mutableStateOf(member.authorities[0]) }
    var heartRateThreshold by remember { mutableStateOf(member.heartRateThreshold.toString()) }

    var date by remember {
        mutableStateOf("${member.year}-${member.month}-${member.day}")
    }

    var expanded by remember { mutableStateOf(false) }
    val authorityOptions = listOf("ADMIN", "EMPLOYEE")

    val isSaveEnabled = name.isNotBlank() && phone.text.isNotEmpty() && email.isNotBlank() &&
            authority.isNotBlank() && heartRateThreshold.isNotBlank() && date.isNotBlank()

    LaunchedEffect(member) {
        name = member.employeeName
        phone = TextFieldValue(member.phone)
        gender = member.gender
        email = member.email
        authority = member.authorities[0]
        heartRateThreshold = member.heartRateThreshold.toString()
        date = "${member.year}-${member.month}-${member.day}"
    }

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
            onValueChange = { newValue ->
                val filteredValue = newValue.text.filter { it.isDigit() }
                val formattedValue = formatPhoneNumber(
                    filteredValue
                )
                phone = newValue.copy(
                    text = formattedValue,
                    selection = TextRange(formattedValue.length)
                )
            },
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

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

        Box {
            OutlinedTextField(
                value = TextFieldValue(authority),
                onValueChange = {},
                readOnly = true,
                label = { Text("Authority") },
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GenderOption(
                text = "남성",
                isSelected = gender == "Male",
                onClick = { gender = "Male" }
            )
            GenderOption(
                text = "여성",
                isSelected = gender == "Female",
                onClick = { gender = "Female" }
            )
        }

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
                        phone = phone.text,
                        gender = gender,
                        email = email,
                        year = selectedYear,
                        month = selectedMonth,
                        day = selectedDay,
                        authorities = listOf(authority),
                        heartRateThreshold = heartRateThreshold.toInt()
                    )
                    onSave(updatedMember)
                },
                modifier = Modifier
                    .weight(3f)
                    .height(62.dp)
                    .background(
                        color = if (isSaveEnabled) Color(0xFFEF151E) else Color.Gray,
                        shape = RoundedCornerShape(size = 8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaveEnabled) Color(0xFFEF151E) else Color.Gray
                ),
                enabled = isSaveEnabled
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

fun formatPhoneNumber(phoneNumber: String): String {
    return when {
        phoneNumber.length <= 3 -> phoneNumber
        phoneNumber.length <= 7 -> "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3)}"
        phoneNumber.length <= 11 -> "${phoneNumber.substring(0, 3)}-${
            phoneNumber.substring(
                3,
                7
            )
        }-${phoneNumber.substring(7)}"

        else -> "${phoneNumber.substring(0, 3)}-${
            phoneNumber.substring(
                3,
                7
            )
        }-${phoneNumber.substring(7, 11)}"
    }
}