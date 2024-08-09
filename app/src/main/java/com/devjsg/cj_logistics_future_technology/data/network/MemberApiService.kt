package com.devjsg.cj_logistics_future_technology.data.network

import com.devjsg.cj_logistics_future_technology.data.model.CheckLoginIdResponse
import com.devjsg.cj_logistics_future_technology.data.model.EditableMember
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateResponse
import com.devjsg.cj_logistics_future_technology.data.model.LoginRequest
import com.devjsg.cj_logistics_future_technology.data.model.LoginResponse
import com.devjsg.cj_logistics_future_technology.data.model.MemberInfoResponse
import com.devjsg.cj_logistics_future_technology.data.model.MemberResponse
import com.devjsg.cj_logistics_future_technology.data.model.MyEmergencyReportResponse
import com.devjsg.cj_logistics_future_technology.data.model.SignUpRequest
import com.devjsg.cj_logistics_future_technology.domain.entity.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.json.Json

class MemberApiService(private val client: HttpClient) {
    suspend fun signUp(signUpRequest: SignUpRequest): HttpResponse {
        return client.post(NetworkConstants.BASE_URL + "member/signup") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json;charset=UTF-8")
            }
            setBody(signUpRequest)
        }
    }

    suspend fun checkLoginId(loginId: String): CheckLoginIdResponse {
        return client.get(NetworkConstants.BASE_URL + "member/check/login-id") {
            url {
                parameters.append("loginId", loginId)
            }
            headers {
                append(HttpHeaders.Accept, "application/json;charset=UTF-8")
            }
        }.body()
    }

    suspend fun login(loginId: String, password: String, fcmToken: String): LoginResponse {
        val response: HttpResponse = client.post(NetworkConstants.BASE_URL + "member/login") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, "application/json;charset=UTF-8")
            }
            setBody(LoginRequest(loginId, password, fcmToken))
        }
        val responseBody = response.bodyAsText()
        return Json.decodeFromString(responseBody)
    }

    suspend fun getMembers(token: String, page: Int, size: Int): MemberResponse {
        val response: HttpResponse = client.get("${NetworkConstants.BASE_URL}member/cursor-paging") {
            header(HttpHeaders.Authorization, "Bearer $token")
            parameter("page", page)
            parameter("size", size)
        }

        val responseBody = response.bodyAsText()
        println("Response Body: $responseBody")
        return response.body()
    }

    suspend fun getMemberInfo(token: String, memberId: String): MemberInfoResponse {
        return client.get("${NetworkConstants.BASE_URL}member/$memberId") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun updateMember(token: String, member: EditableMember): HttpResponse {
        return client.put("${NetworkConstants.BASE_URL}member/manage") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(member)
        }
    }

    suspend fun getEmergencyReports(token: String, start: String, end: String): MyEmergencyReportResponse {
        return client.get("${NetworkConstants.BASE_URL}fcm/employee/emergency-report") {
            header(HttpHeaders.Authorization, "Bearer $token")
            parameter("start", start)
            parameter("end", end)
        }.body()
    }

    suspend fun searchMember(token: String, loginId: String): MemberResponse {
        return client.get("${NetworkConstants.BASE_URL}member/search") {
            header(HttpHeaders.Authorization, "Bearer $token")
            parameter("loginId", loginId)
        }.body()
    }

    suspend fun getHeartRateData(token: String, memberId: Int, start: String, end: String): HeartRateResponse {
        return client.get("${NetworkConstants.BASE_URL}heart-rate/aggregate/$memberId") {
            header("Authorization", "Bearer $token")
            parameter("start", start)
            parameter("end", end)
        }.body()
    }

    suspend fun getMyHeartRateData(token: String, start: String, end: String): HeartRateResponse {
        return client.get("${NetworkConstants.BASE_URL}heart-rate/aggregate") {
            header("Authorization", "Bearer $token")
            parameter("start", start)
            parameter("end", end)
        }.body()
    }

    suspend fun getStaff(
        token: String,
        page: Int,
        offset: Int,
        sorting: String
    ): ApiResponse {
        return try {
            client.get("https://cj-api.serial-blog.com/api/v1/reporting/day-report/v1") {
                header(HttpHeaders.Authorization, "Bearer $token")
                parameter("page", page)
                parameter("offset", offset)
                parameter("report-sorting", sorting)
            }.body()
        } catch (e: JsonConvertException) {
            println("Error parsing JSON: ${e.message}")
            throw e
        } catch (e: Exception) {
            println("Error fetching data: ${e.message}")
            throw e
        }
    }
}