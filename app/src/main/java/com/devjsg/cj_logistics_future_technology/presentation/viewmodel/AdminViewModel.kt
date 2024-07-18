package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.devjsg.cj_logistics_future_technology.data.biometric.KeystoreHelper
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.model.Member
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfo
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetAllMembersUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.GetMemberInfoUseCase
import com.devjsg.cj_logistics_future_technology.domain.usecase.UpdateMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val getMembersUseCase: GetAllMembersUseCase,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val updateMemberUseCase: UpdateMemberUseCase,
    private val dataStoreManager: DataStoreManager,
    private val keystoreHelper: KeystoreHelper
) : ViewModel() {

    val members: Flow<PagingData<Member>> = getMembersUseCase()
        .cachedIn(viewModelScope)

    private val _selectedMember = MutableStateFlow<EditableMember?>(null)
    val selectedMember: StateFlow<EditableMember?> = _selectedMember

    fun getMemberInfo(memberId: String) {
        viewModelScope.launch {
            val response = getMemberInfoUseCase(memberId)
            _selectedMember.value = response.data.toEditableMember()
        }
    }

    fun updateMember(member: EditableMember, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                updateMemberUseCase(member)
                getMembersUseCase()
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
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

fun MemberInfo.toEditableMember(): EditableMember {
    return EditableMember(
        memberId = this.memberId,
        employeeName = this.employeeName,
        phone = this.phone,
        gender = this.gender,
        email = this.email,
        authority = this.authorities.firstOrNull() ?: "",
        year = this.year,
        month = this.month,
        day = this.day
    )
}