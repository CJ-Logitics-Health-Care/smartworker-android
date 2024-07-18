package com.devjsg.cj_logistics_future_technology.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberInfo(
    val memberId: Int,
    var name: String,
    var phone: String,
    var gender: String,
    var email: String,
    var authorities: List<String>,
    var year: Int,
    var month: Int,
    var day: Int
)

@Serializable
data class MemberInfoResponse(
    val data: MemberInfo,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class EditableMemberResponse(
    val data: EditableMember,
    val statusCode: Int,
    val messages: List<String>,
    val success: Boolean
)

@Serializable
data class EditableMember(
    val memberId: Int,
    var employeeName: String,
    var phone: String,
    var gender: String,
    var email: String,
    var authority: String,
    var year: Int,
    var month: Int,
    var day: Int
)