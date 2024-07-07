package com.devjsg.watch.domain

import com.devjsg.watch.data.HeartRateRepository
import com.devjsg.watch.data.MeasureMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GetHeartRateUseCase(private val repository: HeartRateRepository) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<MeasureMessage> {
        return repository.heartRateMeasureFlow()
    }
}