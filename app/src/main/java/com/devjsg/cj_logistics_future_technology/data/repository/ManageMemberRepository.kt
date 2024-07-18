package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfoResponse
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageMemberRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun getMemberInfo(loginId: String): MemberInfoResponse {
        val token = dataStoreManager.token.first() ?: ""
        return apiService.getMemberInfo(token, loginId)
    }

    suspend fun updateMember(member: EditableMember) {
        val token = dataStoreManager.token.first() ?: ""
        apiService.updateMember(token, member)
    }
}