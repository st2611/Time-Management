package com.example.jetpackcompose.screen.statscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpackcompose.R
import com.example.jetpackcompose.viewmodel.StatsViewModel

@Composable
fun StatsScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val priorityDistribution by viewModel.priorityDistribution.collectAsState()
    val dailyCompletionStats by viewModel.dailyCompletionStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.task_completion_statistics),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PieChartComponent(priorityDistribution)

        Spacer(modifier = Modifier.height(24.dp))

        PriorityLegend(priorityDistribution)

        Spacer(modifier = Modifier.height(32.dp))

        BarChartComponent(dailyCompletionStats)
    }
}

@Composable
fun PieChartComponent(priorityDistribution: Map<Int, Int>) {
    val colors = mapOf(
        1 to Color.Red,    // Priority 1 (High)
        2 to Color.Yellow, // Priority 2 (Medium)
        3 to Color.Green   // Priority 3 (Low)
    )

    val pieData = priorityDistribution.map { (priority, count) ->
        PieChartData(count.toFloat(), colors[priority] ?: Color.Gray)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        PieChart(
            data = pieData,
            modifier = Modifier.size(250.dp)
        )
    }
}

@Composable
fun PriorityLegend(priorityDistribution: Map<Int, Int>) {
    val totalTasks = priorityDistribution.values.sum().toFloat().coerceAtLeast(1f)
    val priorities = listOf(
        1 to stringResource(id = R.string.high_priority),
        2 to stringResource(id = R.string.medium_priority),
        3 to stringResource(id = R.string.low_priority)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        priorities.forEach { (priority, label) ->
            val count = priorityDistribution[priority] ?: 0
            val percentage = (count / totalTasks * 100).toInt()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            when (priority) {
                                1 -> Color.Red
                                2 -> Color.Yellow
                                3 -> Color.Green
                                else -> Color.Gray
                            }, shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$percentage%", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}


@Composable
fun BarChartComponent(dailyStats: Map<String, Int>) {
    val sortedStats = dailyStats.toList().sortedBy { it.first }
    val maxCount = sortedStats.maxOfOrNull { it.second } ?: 1
    val barColor = Color(0xFF4CAF50)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Completed Tasks Over The Last 10 Days",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            sortedStats.forEach { (date, count) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((count.toFloat() / maxCount * 140).dp)
                            .background(barColor)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = count.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class PieChartData(
    val value: Float,
    val color: Color
)

@Composable
fun PieChart(
    data: List<PieChartData>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.value.toDouble() }.toFloat()
    var startAngle = 0f

    Canvas(modifier = modifier) {
        data.forEach { pieData ->
            val sweepAngle = (pieData.value / total) * 360f
            drawArc(
                color = pieData.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(size.width, size.height),
                style = Fill
            )
            startAngle += sweepAngle
        }
    }
}

