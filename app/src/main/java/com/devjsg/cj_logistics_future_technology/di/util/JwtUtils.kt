package com.devjsg.cj_logistics_future_technology.di.util

import com.auth0.android.jwt.JWT
import org.json.JSONObject

fun decodeJwt(token: String): String? {
    return try {
        val jwt = JWT(token)
        jwt.getClaim("sub").asString()
    } catch (e: Exception) {
        null
    }
}

fun extractToken(responseBody: String): String? {
    return try {
        val jsonObject = JSONObject(responseBody)
        jsonObject.getJSONObject("data").getString("token")
    } catch (e: Exception) {
        null
    }
}

fun extractRefreshToken(responseBody: String): String? {
    return try {
        val jsonObject = JSONObject(responseBody)
        jsonObject.getJSONObject("data").getString("refreshToken")
    } catch (e: Exception) {
        null
    }
}