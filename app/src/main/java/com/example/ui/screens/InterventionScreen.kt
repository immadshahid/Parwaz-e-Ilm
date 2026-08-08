package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.engine.GeminiReasoningEngine
import com.example.model.InterventionType
import com.example.ui.theme.*

@Composable
fun InterventionScreen(
    studentState: StudentState,
    onSaveIntervention: (InterventionType, String, String, String, String, String) -> Unit,
    onSimulateOutcome: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val student = studentState.student
    val risk = studentState.riskAssessment

    var selectedType by remember { mutableStateOf(InterventionType.TEACHER_FOLLOWUP) }
    var assignedTo by remember { mutableStateOf("Class Teacher (Class 8-A)") }
    var targetDate by remember { mutableStateOf("2026-08-15") }
    var notes by remember { mutableStateOf("Schedule gentle check-in to offer learning support and review attendance habits.") }

    var draftLanguage by remember { mutableStateOf("English") } // "English" or "Urdu"

    val fallbackExp = remember(studentState) {
        GeminiReasoningEngine.generateFallbackExplanation(student, risk)
    }

    var englishDraft by remember { mutableStateOf(fallbackExp.parentMessageDraftEnglish) }
    var urduDraft by remember { mutableStateOf(fallbackExp.parentMessageDraftUrdu) }

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
                    modifier = Modifier.testTag("intervention_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Slate900)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Create Support Intervention",
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

        // Intervention Type Selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SELECT INTERVENTION ACTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald800,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InterventionType.values().forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedType == type),
                                onClick = { selectedType = type },
                                modifier = Modifier.testTag("intervention_type_${type.name}")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = type.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate900
                            )
                        }
                    }
                }
            }
        }

        // Intervention Form Details
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ASSIGNMENT & SCHEDULE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald800,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = assignedTo,
                        onValueChange = { assignedTo = it },
                        label = { Text("Responsible Officer / Educator") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = targetDate,
                        onValueChange = { targetDate = it },
                        label = { Text("Target Follow-up Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Intervention Notes") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Parent Communication Message Draft Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DRAFT PARENT COMMUNICATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Emerald800,
                            letterSpacing = 1.sp
                        )

                        // Language Toggle Switch
                        Row {
                            FilterChip(
                                selected = draftLanguage == "English",
                                onClick = { draftLanguage = "English" },
                                label = { Text("English", fontSize = 11.sp) },
                                modifier = Modifier.testTag("lang_english")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            FilterChip(
                                selected = draftLanguage == "Urdu",
                                onClick = { draftLanguage = "Urdu" },
                                label = { Text("اردو (Urdu)", fontSize = 11.sp) },
                                modifier = Modifier.testTag("lang_urdu")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val activeDraft = if (draftLanguage == "English") englishDraft else urduDraft

                    OutlinedTextField(
                        value = activeDraft,
                        onValueChange = {
                            if (draftLanguage == "English") englishDraft = it else urduDraft = it
                        },
                        label = { Text("Draft Message (Respectful & Supportive)") },
                        minLines = 6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("parent_message_textfield")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Supportive, non-judgmental message created for guardian (${student.guardianName}). Never claims dropout certainty.",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }
        }

        // Save & Outcome Simulation Buttons
        item {
            Button(
                onClick = {
                    onSaveIntervention(
                        selectedType,
                        targetDate,
                        assignedTo,
                        notes,
                        englishDraft,
                        urduDraft
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_intervention_button")
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SAVE INTERVENTION RECORD")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onSimulateOutcome,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold700),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simulate_outcome_button")
            ) {
                Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SIMULATE INTERVENTION IMPACT (BEFORE / AFTER)")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
