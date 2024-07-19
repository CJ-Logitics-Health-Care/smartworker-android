package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetHeartRateDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberDetailViewModel @Inject constructor(
    private val getHeartRateDataUseCase: GetHeartRateDataUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _heartRateData = MutableStateFlow<List<HeartRateData>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateData>> = _heartRateData

    fun getHeartRateData(memberId: Int, start: String, end: String) {
        viewModelScope.launch {
            val response = getHeartRateDataUseCase(memberId, start, end)
            if (response.success) {
                _heartRateData.value = response.data
            } else {
                // Handle error
            }
        }
    }
}