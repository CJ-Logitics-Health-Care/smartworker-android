package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import com.devjsg.cj_logistics_future_technology.data.network.SignUpRequest
import javax.inject.Inject

class MemberRepository @Inject constructor(private val apiService: MemberApiService) {
    suspend fun sendApprovalCode(phone: String) = apiService.sendApprovalCode(phone)
    suspend fun approveCode(phone: String, approvalCode: String) = apiService.approveCode(phone, approvalCode)

    suspend fun signUp(signUpRequest: SignUpRequest) = apiService.signUp(signUpRequest)
}