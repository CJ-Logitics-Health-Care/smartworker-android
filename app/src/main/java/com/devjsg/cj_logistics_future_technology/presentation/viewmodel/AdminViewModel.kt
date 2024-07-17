package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.repository.FindAllMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: FindAllMemberRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val members: Flow<PagingData<Member>> = repository.getMembers()
        .cachedIn(viewModelScope)

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearTokens()
            dataStoreManager.clearHeaderData()
            onLogoutComplete()
        }
    }
}