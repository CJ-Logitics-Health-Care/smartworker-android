package com.devjsg.cj_logistics_future_technology.presentation.home.admin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devjsg.cj_logistics_future_technology.data.model.EmergencyReport

@Composable
fun EmergencyReportItem(report: EmergencyReport, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color(0xFFF7F7F7), shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = "신고자: ${report.reporter}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "위치: (${report.x}, ${report.y})", style = MaterialTheme.typography.bodyMedium)
        Text(text = "긴급 상황: ${report.emergency}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "신고 시간: ${report.createdAt}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "회원 아이디: ${report.loginId}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "전화번호: ${report.phone}", style = MaterialTheme.typography.bodyMedium)
    }
}