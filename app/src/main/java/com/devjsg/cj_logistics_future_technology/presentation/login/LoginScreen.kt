package com.devjsg.cj_logistics_future_technology.presentation.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.devjsg.cj_logistics_future_technology.data.biometric.BiometricPromptHelper
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.MemberViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MemberViewModel = hiltViewModel(),
    biometricPromptHelper: BiometricPromptHelper
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var isBiometricPromptShown by remember { mutableStateOf(false) }

    fun showBiometricPrompt() {
        isBiometricPromptShown = true
        biometricPromptHelper.authenticate(
            onSuccess = {
                isBiometricPromptShown = false
                viewModel.autoLogin { sub ->
                    coroutineScope.launch {
                        Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    if (sub == "1") {
                        navController.navigate("admin_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("worker_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            },
            onError = { errorMessage ->
                isBiometricPromptShown = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("인증이 취소되었습니다.")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = id,
                        onValueChange = { id = it },
                        label = { Text("아이디") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.isFocused && !isBiometricPromptShown) {
                                    showBiometricPrompt()
                                }
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("비밀번호") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.login(id, password, onSuccess = { sub ->
                                coroutineScope.launch {
                                    Toast.makeText(context, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                }
                                if (sub == "1") {
                                    navController.navigate("admin_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    navController.navigate("worker_home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }, onError = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("로그인에 실패했습니다. 아이디나 비밀번호를 다시 확인해주세요.")
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("로그인")
                    }

                    TextButton(
                        onClick = { navController.navigate("sign_up") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("회원가입")
                    }
                }

                if (uiState is MemberViewModel.UiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )

    LaunchedEffect(uiState) {
        when (uiState) {
            is MemberViewModel.UiState.Error -> {
                val errorMessage = (uiState as MemberViewModel.UiState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
            }

            else -> {}
        }
    }
}