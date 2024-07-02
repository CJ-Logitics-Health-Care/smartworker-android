package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.datasync.DataSyncManager
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val dataSyncManager: DataSyncManager,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val heartRate: StateFlow<Int> = dataSyncManager.heartRate

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            onLogoutComplete()
        }
    }
}