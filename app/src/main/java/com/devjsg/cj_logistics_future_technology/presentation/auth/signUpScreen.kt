package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberViewModel
import java.util.regex.Pattern

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(viewModel: MemberViewModel = hiltViewModel<MemberViewModel>()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    val phoneNumber by viewModel.phoneState.collectAsState()
    val approvalCode by viewModel.approvalCodeState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var gender by remember { mutableStateOf("Male") }

    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
    val isPasswordValid by derivedStateOf { Pattern.matches(passwordRegex, password) }
    val doPasswordsMatch by derivedStateOf { password == confirmPassword }

    var isPhoneSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("아이디") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* 중복 확인 로직 */ }) {
                Text("중복 확인")
            }
        }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        if (password.isNotEmpty() && !isPasswordValid) {
            Text(
                text = "비밀번호에는 8자리 이상, 영문, 숫자, 특수 문자를 모두 포함해야 합니다",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("비밀번호 확인") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        if (confirmPassword.isNotEmpty() && !doPasswordsMatch) {
            Text(
                text = "비밀번호와 다릅니다",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일 주소") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        DatePickerIcon(
            date = birthDate,
            onDateSelected = { birthDate = it }
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = viewModel::onPhoneChange,
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            readOnly = isPhoneSubmitted // 전화번호 필드를 수정할 수 없게 설정
        )
        if (!isPhoneSubmitted) {
            Button(
                onClick = {
                    viewModel.sendApprovalCode()
                    isPhoneSubmitted = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("인증 코드 발송")
            }
        }
        if (isPhoneSubmitted) {
            OutlinedTextField(
                value = approvalCode,
                onValueChange = viewModel::onApprovalCodeChange,
                label = { Text("인증 코드") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = { viewModel.approveCode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("인증 코드 확인")
            }
        }

        when (uiState) {
            is MemberViewModel.UiState.Loading -> CircularProgressIndicator()
            is MemberViewModel.UiState.CodeSent -> Text("인증 코드가 발송되었습니다.", color = MaterialTheme.colorScheme.primary)
            is MemberViewModel.UiState.Approved -> Text("인증이 완료되었습니다.", color = MaterialTheme.colorScheme.primary)
            is MemberViewModel.UiState.Error -> {
                Text(
                    (uiState as MemberViewModel.UiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Log.d("SignUpScreen", (uiState as MemberViewModel.UiState.Error).message)
            }
            else -> {}
        }

        Text(
            text = "성별",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioButton(
                selected = (gender == "Male"),
                onClick = { gender = "Male" }
            )
            Text(text = "남성")
            RadioButton(
                selected = (gender == "Female"),
                onClick = { gender = "Female" }
            )
            Text(text = "여성")
        }
        Button(
            onClick = { /* 회원가입 로직 */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("회원가입")
        }
    }
}