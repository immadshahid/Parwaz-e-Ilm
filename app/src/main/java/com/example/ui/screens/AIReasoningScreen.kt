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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.GeminiAIExplanation
import com.example.engine.GeminiReasoningEngine
import com.example.data.StudentState
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AIReasoningScreen(
    studentState: StudentState,
    onCreateIntervention: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val student = studentState.student
    val risk = studentState.riskAssessment
    val scope = rememberCoroutineScope()

    var explanation by remember { mutableStateOf<GeminiAIExplanation?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(studentState) {
        isLoading = true
        explanation = GeminiReasoningEngine.generateExplanation(student, risk)
        isLoading = false
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("ai_reasoning_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Slate900
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Why was ${student.name} flagged?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Layer 2 Gemini Reasoning Engine",
                        fontSize = 11.sp,
                        color = Emerald800,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (isLoading) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Emerald800)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Analyzing cross-signal patterns with Gemini AI...",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate700
                        )
                    }
                }
            }
        } else {
            val exp = explanation!!

            // 1. Cross-Signal Pattern Visualizer Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "CROSS-SIGNAL PATTERN ANALYSIS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald800,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SignalNode(
                            number = 1,
                            title = "Attendance",
                            description = "Current Rate: ${risk.attendanceTrend.currentValue} (${risk.attendanceTrend.description})",
                            status = if (risk.attendanceTrend.changePercentage < 0) "Significant Decline" else "Stable",
                            isNegative = risk.attendanceTrend.changePercentage < 0
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SignalNode(
                            number = 2,
                            title = "Academic Performance",
                            description = "Assessment Average: ${risk.academicTrend.currentValue} (${risk.academicTrend.description})",
                            status = if (risk.academicTrend.changePercentage < 0) "Declining" else "Stable",
                            isNegative = risk.academicTrend.changePercentage < 0
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SignalNode(
                            number = 3,
                            title = "Fee Payment Status",
                            description = "Installment Status: ${risk.feeTrend.currentValue} (${risk.feeTrend.description})",
                            status = if (risk.feeTrend.currentValue != "Paid") "Recent Delays" else "Current",
                            isNegative = risk.feeTrend.currentValue != "Paid"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (risk.crossSignalDeterioration) RiskEarlyWarningBg else RiskOnTrackBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LinearScale,
                                    contentDescription = "Cross Signal",
                                    tint = if (risk.crossSignalDeterioration) RiskEarlyWarning else RiskOnTrack
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (risk.crossSignalDeterioration)
                                        "Cross-Signal Deterioration: Multiple indicators changed during the same period."
                                    else
                                        "Single-signal variation noted; indicators are mostly independent.",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (risk.crossSignalDeterioration) RiskEarlyWarning else RiskOnTrack
                                )
                            }
                        }
                    }
                }
            }

            // 2. Parwaaz AI Assessment Card
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
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Assessment",
                                tint = Gold600,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PARWAAZ AI ASSESSMENT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = exp.summary,
                            fontSize = 14.sp,
                            color = Slate900,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Detailed Pattern Analysis:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate700
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = exp.whyFlagged,
                            fontSize = 13.sp,
                            color = Slate700,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 3. Recommended Supportive Action
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Emerald50),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Emerald100)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "RECOMMENDED ACTION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald800,
                                letterSpacing = 1.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Emerald800
                            ) {
                                Text(
                                    text = exp.interventionPriority,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = exp.recommendedAction,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald900,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Action Button
            item {
                Button(
                    onClick = onCreateIntervention,
                    colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("proceed_to_intervention_button")
                ) {
                    Icon(imageVector = Icons.Default.Handshake, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PROCEED TO CREATE SUPPORT INTERVENTION")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SignalNode(
    number: Int,
    title: String,
    description: String,
    status: String,
    isNegative: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate100, shape = RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = if (isNegative) RiskEarlyWarningBg else RiskOnTrackBg,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "$number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isNegative) RiskEarlyWarning else RiskOnTrack
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = Slate500
            )
        }

        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isNegative) RiskEarlyWarning else RiskOnTrack
        )
    }
}
