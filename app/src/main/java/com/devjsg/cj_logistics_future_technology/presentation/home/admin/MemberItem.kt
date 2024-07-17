package com.devjsg.cj_logistics_future_technology.presentation.home.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.devjsg.cj_logistics_future_technology.data.model.Member

@Composable
fun MemberItem(member: Member) {
    Column {
        Text(text = "Member ID: ${member.memberId}")
        Text(text = "Gender: ${member.gender}")
        Text(text = "Authorities: ${member.authorities.joinToString()}")
        Text(text = "Date of Birth: ${member.year}-${member.month}-${member.day}")
        Text(text = "Created At: ${member.createdAt}")
    }
}