package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.repository.AdminMemberRepository
import com.devjsg.cj_logistics_future_technology.domain.entity.Staff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContestHomeViewModel @Inject constructor(
    private val memberRepository: AdminMemberRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<Staff>>(emptyList())
    val staffList: StateFlow<List<Staff>> = _staffList

    var currentPage = mutableStateOf(1)
    var sorting = mutableStateOf("MOVE_DESC")

    fun loadStaff(page: Int, sorting: String, listSize: Int) {
        viewModelScope.launch {
            val token = dataStoreManager.token.first()
            val response = memberRepository.getStaff(token!!, page, listSize, sorting)
            _staffList.value = response.data.value
        }
    }
}