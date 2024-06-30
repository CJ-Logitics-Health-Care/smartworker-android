package com.devjsg.cj_logistics_future_technology.data.network

import com.devjsg.cj_logistics_future_technology.NetworkConstants
import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class MemberApiService(private val client: HttpClient) {
    suspend fun sendApprovalCode(phone: String): HttpResponse {
        return client.post(NetworkConstants.BASE_URL + "member/send/approval-code") {
            url {
                parameters.append("phone", phone)
            }
            headers {
                append(HttpHeaders.Accept, "application/json;charset=UTF-8")
            }
        }
    }

    suspend fun approveCode(phone: String, approvalCode: String): HttpResponse {
        return client.put(NetworkConstants.BASE_URL + "member/approve") {
            url {
                parameters.append("phone", phone)
                parameters.append("approvalCode", approvalCode)
            }
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): HttpResponse {
        return client.post(NetworkConstants.BASE_URL + "member/signup") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json;charset=UTF-8")
            }
            setBody(signUpRequest)
        }
    }
}