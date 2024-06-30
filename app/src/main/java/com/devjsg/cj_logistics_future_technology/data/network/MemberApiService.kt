package com.devjsg.cj_logistics_future_technology.data.network

import com.devjsg.cj_logistics_future_technology.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI

class MemberApiService(private val client: HttpClient) {
    @OptIn(InternalAPI::class)
    suspend fun sendApprovalCode(phone: String): HttpResponse {
        return client.post(NetworkConstants.BASE_URL + "member/send/approval-code") {
            contentType(ContentType.Application.Json)
            body = mapOf("phone" to phone)
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun approveCode(phone: String, approvalCode: String): HttpResponse {
        return client.put(NetworkConstants.BASE_URL + "member/approve") {
            contentType(ContentType.Application.Json)
            body = mapOf("phone" to phone, "approvalCode" to approvalCode)
        }
    }
}