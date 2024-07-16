package com.devjsg.cj_logistics_future_technology.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val heartRate by viewModel.heartRate.collectAsState()
    val isMonitoring by viewModel.isMonitoring.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = if (heartRate != 0) heartRate.toString() else "심박수 모니터",
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.body1.fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.2.sp,
                    color = Color.Black,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        width = 1.2.dp,
                        color = Color(0xFFDFDFDF),
                        shape = RoundedCornerShape(size = 9.6.dp)
                    )
                    .width(144.dp)
                    .height(54.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(size = 9.6.dp))
                    .padding(vertical = 17.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(19.2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (isMonitoring) {
                            viewModel.stopService()
                        } else {
                            viewModel.startService()
                        }
                    },
                    modifier = Modifier
                        .width(67.2.dp)
                        .height(80.4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF242424)),
                    shape = RoundedCornerShape(9.6.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isMonitoring) R.drawable.ic_stop else R.drawable.ic_heart
                        ),
                        contentDescription = if (isMonitoring) "Stop Monitoring" else "Start Monitoring",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = { viewModel.report() },
                    modifier = Modifier
                        .width(67.2.dp)
                        .height(80.4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF151E)),
                    shape = RoundedCornerShape(9.6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_report),
                        contentDescription = "Report",
                        tint = Color.White
                    )
                }
            }
        }
    }
}