package com.devjsg.cj_logistics_future_technology.data.repository

import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.model.LoginResponse
import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.data.network.MemberApiService
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val apiService: MemberApiService,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun signUp(signUpRequest: SignUpRequest) = apiService.signUp(signUpRequest)
    suspend fun checkLoginId(loginId: String) = apiService.checkLoginId(loginId)

    suspend fun login(loginId: String, password: String): LoginResponse {
        val response = apiService.login(loginId, password)
        if (response.success) {
            dataStoreManager.saveToken(response.data.token, response.data.refreshToken)
        }
        return response
    }
}