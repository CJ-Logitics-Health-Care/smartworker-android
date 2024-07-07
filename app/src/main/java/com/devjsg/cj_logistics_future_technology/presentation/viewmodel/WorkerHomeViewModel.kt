package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.presentation.home.WearableConnectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository,
    private val connectionManager: WearableConnectionManager,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _heartRateAvg = MutableStateFlow<Int>(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    private val _isConnected = MutableStateFlow<Boolean?>(null)
    val isConnected: StateFlow<Boolean?> get() = _isConnected

    init {
        checkConnection()
    }

    private fun checkConnection() {
        viewModelScope.launch {
            connectionManager.isAnyDeviceConnected().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isConnected.value = task.result
                } else {
                    _isConnected.value = false
                }
            }
        }
    }

    fun setHeartRateAvg(heartRateAvg: Int) {
        _heartRateAvg.value = heartRateAvg
        // 수신된 데이터를 Repository를 통해 처리
        heartRateRepository.handleReceivedHeartRateAvg(heartRateAvg)
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            onLogoutComplete()
        }
    }
}