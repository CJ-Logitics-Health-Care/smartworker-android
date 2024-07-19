package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.MyEmergencyReport
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMyEmergencyReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository,
    private val dataStoreManager: DataStoreManager,
    private val keystoreHelper: KeystoreHelper,
    private val getMyEmergencyReportsUseCase: GetMyEmergencyReportsUseCase
) : ViewModel() {

    private val _heartRateAvg = MutableStateFlow<Int>(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    private val _myEmergencyReports = MutableStateFlow<List<MyEmergencyReport>>(emptyList())
    val myEmergencyReports: StateFlow<List<MyEmergencyReport>> = _myEmergencyReports

    fun setHeartRateAvg(heartRateAvg: Int) {
        _heartRateAvg.value = heartRateAvg
        Log.d("WorkerHomeViewModel", "setHeartRateAvg: ${_heartRateAvg.value}")
        heartRateRepository.handleReceivedHeartRateAvg(heartRateAvg)
    }

    private fun loadEmergencyReports() {
        viewModelScope.launch {
            val reports = getMyEmergencyReportsUseCase()
            _myEmergencyReports.value = reports
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            dataStoreManager.clearHeaderData()
            keystoreHelper.clearLoginData()
            onLogoutComplete()
        }
    }
}