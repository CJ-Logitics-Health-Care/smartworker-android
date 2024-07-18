package com.devjsg.cj_logistics_future_technology.domain.usecase

import com.devjsg.cj_logistics_future_technology.data.model.MyEmergencyReport
import com.devjsg.cj_logistics_future_technology.data.repository.MyEmergencyReportRepository
import javax.inject.Inject

class GetMyEmergencyReportsUseCase @Inject constructor(
    private val repository: MyEmergencyReportRepository
) {
    suspend operator fun invoke(): List<MyEmergencyReport> {
        return repository.getEmergencyReports()
    }
}