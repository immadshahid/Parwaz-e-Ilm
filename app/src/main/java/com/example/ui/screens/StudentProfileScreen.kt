package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.SignalTrend
import com.example.ui.components.RiskBadge
import com.example.ui.components.TrendIndicator
import com.example.ui.theme.*

@Composable
fun StudentProfileScreen(
    studentState: StudentState,
    onViewAIReasoning: () -> Unit,
    onCreateIntervention: () -> Unit,
    onViewImpact: (() -> Unit)?,
    onOpenAttendanceDialog: () -> Unit,
    onOpenScoreDialog: () -> Unit,
    onOpenFeeDialog: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val student = studentState.student
    val risk = studentState.riskAssessment

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate900
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Student Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }
        }

        // Student Overview Header Card
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
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Emerald100,
                                modifier = Modifier.size(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = student.name.take(1),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp,
                                        color = Emerald900
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = student.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "Class ${student.className} • Section ${student.section}",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                                Text(
                                    text = "Guardian: ${student.guardianName} (${student.guardianContact})",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${risk.score}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (risk.level) {
                                    EarlyWarningLevel.EARLY_WARNING -> RiskEarlyWarning
                                    EarlyWarningLevel.ELEVATED -> RiskElevated
                                    EarlyWarningLevel.ATTENTION -> RiskAttention
                                    EarlyWarningLevel.ON_TRACK -> RiskOnTrack
                                }
                            )
                            Text(
                                text = "Risk Score",
                                fontSize = 10.sp,
                                color = Slate500
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RiskBadge(level = risk.level)
                        if (studentState.isInterventionActive) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Emerald100
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Active Support",
                                        tint = Emerald800,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Intervention Active",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Emerald800
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Data Logging Buttons Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenAttendanceDialog,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Attendance", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = onOpenScoreDialog,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Grade, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Score", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = onOpenFeeDialog,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fee", fontSize = 11.sp)
                }
            }
        }

        // 3 Longitudinal Signal Cards
        item {
            Text(
                text = "LONGITUDINAL SIGNALS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Slate500,
                letterSpacing = 1.sp
            )
        }

        // Signal Card 1: Attendance
        item {
            SignalDetailCard(
                title = "ATTENDANCE",
                icon = Icons.Default.EventAvailable,
                trend = risk.attendanceTrend,
                metricLabel = "Current Attendance Rate"
            )
        }

        // Signal Card 2: Academic Performance
        item {
            SignalDetailCard(
                title = "ACADEMIC PERFORMANCE",
                icon = Icons.Default.School,
                trend = risk.academicTrend,
                metricLabel = "Recent Assessment Average"
            )
        }

        // Signal Card 3: Fee Status
        item {
            SignalDetailCard(
                title = "FEE PAYMENT PATTERN",
                icon = Icons.Default.Payments,
                trend = risk.feeTrend,
                metricLabel = "Payment Installment Status"
            )
        }

        // WHY PARWAAZ FLAGGED THIS STUDENT
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReportProblem,
                            contentDescription = "Flagged Reason",
                            tint = if (risk.score >= 50) RiskEarlyWarning else Gold600,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WHY PARWAAZ FLAGGED THIS STUDENT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    risk.evidenceList.forEach { evidence ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", fontWeight = FontWeight.Bold, color = Slate700)
                            Text(
                                text = evidence,
                                fontSize = 13.sp,
                                color = Slate700,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onViewAIReasoning,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("view_ai_reasoning_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Reasoning",
                            tint = Gold500,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VIEW AI REASONING & RECOMMENDATION")
                    }

                    if (studentState.outcome != null && onViewImpact != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onViewImpact,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("view_impact_button")
                        ) {
                            Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VIEW INTERVENTION IMPACT (BEFORE / AFTER)")
                        }
                    }
                }
            }
        }

        // Action: Create Support Intervention Button
        item {
            Button(
                onClick = onCreateIntervention,
                colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_intervention_button")
            ) {
                Icon(imageVector = Icons.Default.Handshake, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("START SUPPORT INTERVENTION")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SignalDetailCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    trend: SignalTrend,
    metricLabel: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Emerald800,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700,
                        letterSpacing = 0.5.sp
                    )
                }
                TrendIndicator(trend = trend.trend)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = metricLabel,
                        fontSize = 11.sp,
                        color = Slate500
                    )
                    Text(
                        text = trend.currentValue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Text(
                    text = trend.description,
                    fontSize = 11.sp,
                    color = Slate700,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }
        }
    }
}
