package com.devjsg.cj_logistics_future_technology.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HeartRateApiService(private val httpClient: HttpClient) {
    suspend fun sendHeartRateData(heartRateAvg: Int) {
        httpClient.post("https://your.server.url/heart_rate") {
            contentType(ContentType.Application.Json)
            setBody(HeartRateDataModel(heartRateAvg))
        }
    }
}

data class HeartRateDataModel(val heartRateAvg: Int)