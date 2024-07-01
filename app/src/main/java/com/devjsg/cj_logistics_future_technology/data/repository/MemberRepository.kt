package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import javax.inject.Inject

class MemberRepository @Inject constructor(private val apiService: MemberApiService) {
    suspend fun signUp(signUpRequest: SignUpRequest) = apiService.signUp(signUpRequest)
    suspend fun checkLoginId(loginId: String) = apiService.checkLoginId(loginId)
}