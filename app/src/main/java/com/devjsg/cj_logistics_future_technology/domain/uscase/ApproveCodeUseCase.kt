package com.devjsg.cj_logistics_future_technology.domain.uscase

import com.devjsg.cj_logistics_future_technology.data.repository.MemberRepository
import javax.inject.Inject

class ApproveCodeUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(phone: String, approvalCode: String) = repository.approveCode(phone, approvalCode)
}