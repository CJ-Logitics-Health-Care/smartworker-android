package com.devjsg.cj_logistics_future_technology.domain.uscase

import com.devjsg.cj_logistics_future_technology.data.network.SignUpRequest
import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(signUpRequest: SignUpRequest) = repository.signUp(signUpRequest)
}