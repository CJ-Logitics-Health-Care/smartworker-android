package com.devjsg.cj_logistics_future_technology.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.devjsg.cj_logistics_future_technology.data.model.HeartRateData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun HeartRateChart(heartRateData: List<HeartRateData>) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setMaxVisibleValueCount(60)
                setPinchZoom(false)
                setDrawBarShadow(false)
                setDrawGridBackground(false)

                val xAxis = xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)

                val leftAxis = axisLeft
                leftAxis.setDrawGridLines(false)

                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val entries = heartRateData.map {
                BarEntry(it.dateTime.toFloat(), it.averageHeartRate.toFloat())
            }
            val dataSet = BarDataSet(entries, "Average Heart Rate")
            val data = BarData(dataSet)
            chart.data = data
            chart.invalidate()
        }
    )
}