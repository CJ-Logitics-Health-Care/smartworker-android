package com.devjsg.cj_logistics_future_technology.presentation.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberViewModel
import java.util.regex.Pattern

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    viewModel: MemberViewModel = hiltViewModel<MemberViewModel>(),
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var employeeName by remember { mutableStateOf("") }
    val isLoginIdValid by viewModel.isLoginIdValid.collectAsState()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    val phoneNumber by viewModel.phoneState.collectAsState()
    var gender by remember { mutableStateOf("Male") }
    val loginIdMessage by viewModel.loginIdMessage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
    val isPasswordValid by derivedStateOf { Pattern.matches(passwordRegex, password) }
    val doPasswordsMatch by derivedStateOf { password == confirmPassword }
    val isFormValid by derivedStateOf {
        username.isNotEmpty() && employeeName.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && email.isNotEmpty() && birthDate.isNotEmpty() &&
                phoneNumber.isNotEmpty() && isPasswordValid &&
                doPasswordsMatch && isLoginIdValid
    }
    val context = LocalContext.current

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
            Button(onClick = { viewModel.checkLoginId(username) }) {
                Text("중복 확인")
            }
        }
        if (loginIdMessage.isNotEmpty()) {
            Text(
                text = loginIdMessage,
                color = if (isLoginIdValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = employeeName,
            onValueChange = { employeeName = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
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
            onClick = {
                if (isFormValid) {
                    val dateParts = birthDate.split("-")
                    val year = dateParts[0].toInt()
                    val month = dateParts[1].toInt()
                    val day = dateParts[2].toInt()

                    viewModel.signUp(
                        loginId = username,
                        password = password,
                        phone = phoneNumber,
                        gender = if (gender == "Male") "MALE" else "FEMALE",
                        email = email,
                        employeeName = employeeName,
                        year = year,
                        month = month,
                        day = day,
                        onSuccess = {
                            Toast.makeText(context, "회원가입이 성공적으로 완료되었습니다", Toast.LENGTH_LONG).show()
                            navController.navigate("login")
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, "회원가입 실패: $errorMessage", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                } else {
                    Toast.makeText(context, "모든 필드를 올바르게 작성해주세요.", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text("회원가입")
        }
    }
}