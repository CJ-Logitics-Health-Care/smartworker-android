package com.devjsg.cj_logistics_future_technology.domain.uscase

import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(id: String, password: String) = repository.login(id, password)
}