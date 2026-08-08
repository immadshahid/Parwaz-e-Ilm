package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentState
import com.example.model.EarlyWarningLevel
import com.example.model.Intervention
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    studentsState: List<StudentState>,
    interventions: List<Intervention>,
    onSelectStudent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalStudents = studentsState.size
    val earlyWarningStudents = studentsState.filter { it.riskAssessment.level == EarlyWarningLevel.EARLY_WARNING }
    val elevatedStudents = studentsState.filter { it.riskAssessment.level == EarlyWarningLevel.ELEVATED }
    val activeInterventionsCount = studentsState.count { it.isInterventionActive }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Banner Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald900),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SCHOOL ADMINISTRATION & COUNSELOR PORTAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold500,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Institutional Early-Warning Overview",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AdminStatBox("Total Students", "$totalStudents", Emerald800, Color.White)
                        AdminStatBox("Early Warning", "${earlyWarningStudents.size}", RiskEarlyWarningBg, RiskEarlyWarning)
                        AdminStatBox("Active Support", "$activeInterventionsCount", RiskAttentionBg, RiskAttention)
                    }
                }
            }
        }

        // Class-Level Breakdown Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CLASS-LEVEL RISK DISTRIBUTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald800,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ClassRiskRow("Class 8-A (Assigned)", totalStudents, earlyWarningStudents.size, elevatedStudents.size)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)
                    ClassRiskRow("Class 8-B", 30, 0, 2)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)
                    ClassRiskRow("Class 9-A", 34, 1, 3)
                }
            }
        }

        // Priority Intervention Support Queue
        item {
            Text(
                text = "PRIORITY EARLY-WARNING QUEUE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Slate500,
                letterSpacing = 1.sp
            )
        }

        if (earlyWarningStudents.isEmpty() && elevatedStudents.isEmpty()) {
            item {
                Text(
                    text = "No high priority early-warning cases recorded across classes.",
                    fontSize = 13.sp,
                    color = Slate500
                )
            }
        } else {
            items(earlyWarningStudents + elevatedStudents, key = { it.student.id }) { state ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                    onClick = { onSelectStudent(state.student.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_queue_item_${state.student.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Emerald100,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = state.student.name.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald900
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.student.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "Class ${state.student.className} • Score: ${state.riskAssessment.score}",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        RiskBadge(level = state.riskAssessment.level)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatBox(label: String, count: String, bgColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = textColor.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun ClassRiskRow(className: String, total: Int, warningCount: Int, elevatedCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = className, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
            Text(text = "$total enrolled students", fontSize = 11.sp, color = Slate500)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (warningCount > 0) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RiskEarlyWarningBg
                ) {
                    Text(
                        text = "$warningCount Early Warning",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RiskEarlyWarning,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            if (elevatedCount > 0) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RiskElevatedBg
                ) {
                    Text(
                        text = "$elevatedCount Elevated",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RiskElevated,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            if (warningCount == 0 && elevatedCount == 0) {
                Text(text = "On Track", fontSize = 11.sp, color = RiskOnTrack)
            }
        }
    }
}
