package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentState
import com.example.model.EarlyWarningLevel
import com.example.ui.components.ParwaazInsightBanner
import com.example.ui.components.RiskBadge
import com.example.ui.components.TrendIndicator
import com.example.ui.theme.*

@Composable
fun TeacherDashboardScreen(
    studentsState: List<StudentState>,
    onSelectStudent: (String) -> Unit,
    onNavigateToClassPulse: () -> Unit,
    onOpenAttendanceDialog: (StudentState) -> Unit,
    onOpenScoreDialog: (StudentState) -> Unit,
    onOpenFeeDialog: (StudentState) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalStudents = studentsState.size
    val onTrackCount = studentsState.count { it.riskAssessment.level == EarlyWarningLevel.ON_TRACK }
    val attentionCount = studentsState.count { it.riskAssessment.level == EarlyWarningLevel.ATTENTION }
    val elevatedCount = studentsState.count { it.riskAssessment.level == EarlyWarningLevel.ELEVATED }
    val earlyWarningCount = studentsState.count { it.riskAssessment.level == EarlyWarningLevel.EARLY_WARNING }

    val flaggedStudents = studentsState.filter {
        it.riskAssessment.level == EarlyWarningLevel.EARLY_WARNING || it.riskAssessment.level == EarlyWarningLevel.ELEVATED
    }.sortedByDescending { it.riskAssessment.score }

    val aminaState = studentsState.find { it.student.name.contains("Amina", ignoreCase = true) }
    val aminaInsight = aminaState?.riskAssessment?.trendSummary
        ?: "Amina's attendance and academic performance have declined together for five consecutive weeks."

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Class Header Stats Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CLASS 8-A",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald800,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Class Progress Overview",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = Emerald50,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "$totalStudents",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Emerald800
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricPill("On Track", onTrackCount, RiskOnTrackBg, RiskOnTrack)
                        MetricPill("Attention", attentionCount, RiskAttentionBg, RiskAttention)
                        MetricPill("Elevated", elevatedCount, RiskElevatedBg, RiskElevated)
                        MetricPill("Warning", earlyWarningCount, RiskEarlyWarningBg, RiskEarlyWarning)
                    }
                }
            }
        }

        // 2. Parwaaz AI Insight Hero Banner
        item {
            ParwaazInsightBanner(
                title = "PARWAAZ INSIGHT • CLASS 8-A",
                insightText = aminaInsight,
                onClick = {
                    aminaState?.let { onSelectStudent(it.student.id) }
                },
                modifier = Modifier.testTag("ai_insight_banner")
            )
        }

        // 3. Quick Actions Grid
        item {
            Text(
                text = "Quick Class Logging",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val primaryTarget = aminaState ?: studentsState.firstOrNull()
                QuickActionButton(
                    label = "Attendance",
                    icon = Icons.Default.EventAvailable,
                    onClick = { primaryTarget?.let { onOpenAttendanceDialog(it) } },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_attendance_button")
                )
                QuickActionButton(
                    label = "Scores",
                    icon = Icons.Default.Grade,
                    onClick = { primaryTarget?.let { onOpenScoreDialog(it) } },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_score_button")
                )
                QuickActionButton(
                    label = "Fee Status",
                    icon = Icons.Default.Payments,
                    onClick = { primaryTarget?.let { onOpenFeeDialog(it) } },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("quick_fee_button")
                )
            }
        }

        // 4. Students Needing Attention Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Students Needing Support",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                TextButton(
                    onClick = onNavigateToClassPulse,
                    modifier = Modifier.testTag("view_all_students_button")
                ) {
                    Text("View All ($totalStudents)", fontSize = 12.sp, color = Emerald800)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View All",
                        tint = Emerald800,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        if (flaggedStudents.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = RiskOnTrackBg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "All Clear",
                            tint = RiskOnTrack
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "All students in Class 8-A are currently on track!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = RiskOnTrack
                        )
                    }
                }
            }
        } else {
            items(flaggedStudents, key = { it.student.id }) { state ->
                StudentSummaryCard(
                    studentState = state,
                    onClick = { onSelectStudent(state.student.id) }
                )
            }
        }
    }
}

@Composable
fun MetricPill(label: String, count: Int, bgColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier.width(72.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                fontSize = 16.sp,
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
fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CardSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Emerald800,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
        }
    }
}

@Composable
fun StudentSummaryCard(
    studentState: StudentState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("student_card_${studentState.student.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Emerald100,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = studentState.student.name.take(1),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Emerald900
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = studentState.student.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Score: ${studentState.riskAssessment.score}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RiskBadge(level = studentState.riskAssessment.level)
                    TrendIndicator(trend = studentState.riskAssessment.attendanceTrend.trend)
                }
            }
        }
    }
}
