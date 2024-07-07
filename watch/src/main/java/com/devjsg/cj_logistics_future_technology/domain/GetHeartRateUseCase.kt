package com.devjsg.cj_logistics_future_technology.domain

import com.devjsg.cj_logistics_future_technology.data.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.data.MeasureMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GetHeartRateUseCase(private val repository: HeartRateRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<MeasureMessage> {
        return repository.heartRateMeasureFlow()
    }
}