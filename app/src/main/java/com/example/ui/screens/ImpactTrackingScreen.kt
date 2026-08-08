package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentState
import com.example.model.EarlyWarningLevel
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*

@Composable
fun ImpactTrackingScreen(
    studentState: StudentState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val student = studentState.student
    val outcome = studentState.outcome

    val attBefore = outcome?.attendanceBefore ?: 68.0
    val attAfter = outcome?.attendanceAfter ?: 84.0
    val perfBefore = outcome?.performanceBefore ?: 59.0
    val perfAfter = outcome?.performanceAfter ?: 71.0
    val feeBefore = outcome?.feeStatusBefore ?: "Delayed"
    val feeAfter = outcome?.feeStatusAfter ?: "Paid"
    val scoreBefore = outcome?.riskBefore ?: 84
    val scoreAfter = outcome?.riskAfter ?: 42

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("impact_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Slate900)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Intervention Impact Analysis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Student: ${student.name} (Class ${student.className})",
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Hero Impact Result Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Emerald900),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Emerald800,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Impact",
                                tint = Gold500,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "INTERVENTION SUCCESSFUL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold500,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Student indicators improved significantly after support intervention.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Side-by-Side BEFORE vs AFTER Comparison Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LONGITUDINAL BEFORE & AFTER COMPARISON",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald800,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // BEFORE Column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(RiskEarlyWarningBg, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "BEFORE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RiskEarlyWarning
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Attendance", fontSize = 10.sp, color = Slate500)
                            Text("${attBefore.toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Performance", fontSize = 10.sp, color = Slate500)
                            Text("${perfBefore.toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Fee Status", fontSize = 10.sp, color = Slate500)
                            Text(feeBefore, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskEarlyWarning)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Risk Score", fontSize = 10.sp, color = Slate500)
                            Text("$scoreBefore", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RiskEarlyWarning)
                            RiskBadge(level = EarlyWarningLevel.EARLY_WARNING, showUrdu = false)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // AFTER Column
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(RiskOnTrackBg, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "AFTER SUPPORT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RiskOnTrack
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Attendance", fontSize = 10.sp, color = Slate500)
                            Text("${attAfter.toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RiskOnTrack)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Performance", fontSize = 10.sp, color = Slate500)
                            Text("${perfAfter.toInt()}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RiskOnTrack)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Fee Status", fontSize = 10.sp, color = Slate500)
                            Text(feeAfter, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskOnTrack)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Risk Score", fontSize = 10.sp, color = Slate500)
                            Text("$scoreAfter", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RiskAttention)
                            RiskBadge(level = EarlyWarningLevel.ATTENTION, showUrdu = false)
                        }
                    }
                }
            }
        }

        // Impact Narrative & Explanation Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Emerald800)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PROGRESS SUMMARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = outcome?.narrative
                            ?: "Attendance improved for three consecutive weeks and assessment performance recovered following counselor and parent check-in.",
                        fontSize = 13.sp,
                        color = Slate700,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Return Button
        item {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("return_to_profile_button")
            ) {
                Text("RETURN TO STUDENT PROFILE")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
