package com.devjsg.cj_logistics_future_technology.data.repository

import android.content.Context
import android.util.Log
import javax.inject.Inject

class HeartRateRepository @Inject constructor(
    private val context: Context
) {
    fun handleReceivedHeartRateAvg(heartRateAvg: Int) {
        // 수신된 평균 심박수를 처리하는 로직을 여기에 구현합니다.
        // 예: 로컬 데이터베이스에 저장하거나 UI를 업데이트하기 위해 ViewModel에 전달
        Log.d("HeartRateRepository", "Received heart rate avg: $heartRateAvg")
    }
}