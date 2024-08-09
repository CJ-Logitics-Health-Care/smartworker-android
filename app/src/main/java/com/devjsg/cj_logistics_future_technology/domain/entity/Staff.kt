package com.devjsg.cj_logistics_future_technology.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Staff (
    val id: Int,
    val memberName: String,
    val moveWork: Int,
    val heartRate:Float,
    val km:Float,
    val createdAt: String
)

@Serializable
data class ApiResponse(
    val data: Data,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class Data(
    val nowPage: Int,
    val allPageCount: Int,
    val value: List<Staff>
)