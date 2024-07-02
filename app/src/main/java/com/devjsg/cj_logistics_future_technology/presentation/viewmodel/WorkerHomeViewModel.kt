package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.devjsg.cj_logistics_future_technology.data.datasync.DataSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WorkerHomeViewModel @Inject constructor(
    private val dataSyncManager: DataSyncManager
) : ViewModel() {

    val heartRate: StateFlow<Int> = dataSyncManager.heartRate
}