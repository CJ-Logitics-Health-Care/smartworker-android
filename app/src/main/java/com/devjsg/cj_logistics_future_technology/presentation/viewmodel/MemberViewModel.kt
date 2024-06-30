package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.domain.uscase.ApproveCodeUseCase
import com.devjsg.cj_logistics_future_technology.domain.uscase.SendApprovalCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val sendApprovalCodeUseCase: SendApprovalCodeUseCase,
    private val approveCodeUseCase: ApproveCodeUseCase
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

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object CodeSent : UiState()
        object Approved : UiState()
        data class Error(val message: String) : UiState()
    }
}