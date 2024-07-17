package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val data: MemberData,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class MemberData(
    val value: List<Member>,
    val hasNext: Boolean,
    val lastIndex: Int
)

@Serializable
data class Member(
    val memberId: Int,
    val gender: String,
    val authorities: List<String>,
    val year: Int,
    val month: Int,
    val day: Int,
    val createdAt: String
)