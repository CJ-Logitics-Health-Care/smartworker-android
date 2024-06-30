package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.domain.uscase.ApproveCodeUseCase
import com.devjsg.cj_logistics_future_technology.domain.uscase.SendApprovalCodeUseCase
import com.devjsg.cj_logistics_future_technology.domain.uscase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val sendApprovalCodeUseCase: SendApprovalCodeUseCase,
    private val approveCodeUseCase: ApproveCodeUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _phoneState = MutableStateFlow("")
    val phoneState: StateFlow<String> = _phoneState

    private val _approvalCodeState = MutableStateFlow("")
    val approvalCodeState: StateFlow<String> = _approvalCodeState

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun onPhoneChange(newPhone: String) {
        _phoneState.value = newPhone
    }

    fun onApprovalCodeChange(newCode: String) {
        _approvalCodeState.value = newCode
    }

    fun sendApprovalCode() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = sendApprovalCodeUseCase(phoneState.value)
                if (response.status.isSuccess()) {
                    _uiState.value = UiState.CodeSent
                } else {
                    _uiState.value = UiState.Error("Failed to send code")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun approveCode() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = approveCodeUseCase(phoneState.value, approvalCodeState.value)
                if (response.status.isSuccess()) {
                    _uiState.value = UiState.Approved
                } else {
                    _uiState.value = UiState.Error("Failed to approve code")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signUp(
        loginId: String,
        password: String,
        phone: String,
        gender: String,
        email: String,
        employeeName: String,
        year: Int,
        month: Int,
        day: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val signUpRequest = SignUpRequest(
                    loginId = loginId,
                    password = password,
                    phone = phone,
                    gender = gender,
                    email = email,
                    employeeName = employeeName,
                    year = year,
                    month = month,
                    day = day
                )
                val response = signUpUseCase(signUpRequest)
                if (response.status.isSuccess()) {
                    onSuccess()
                } else {
                    _uiState.value = UiState.Error("Failed to sign up")
                    onError("Failed to sign up")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
                onError(e.message ?: "Unknown error")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object CodeSent : UiState()
        object Approved : UiState()
        data class Error(val message: String) : UiState()
    }
}