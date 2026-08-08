package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Student
import com.example.ui.theme.Emerald800

@Composable
fun RecordAttendanceDialog(
    student: Student,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf("Present") }
    val statuses = listOf("Present", "Late", "Absent")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Record Attendance",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Record daily attendance for ${student.name} (Class ${student.className}):",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                statuses.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedStatus == status),
                                onClick = { selectedStatus = status }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedStatus == status),
                            onClick = { selectedStatus = status }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = status, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedStatus) },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                modifier = Modifier.testTag("submit_attendance_button")
            ) {
                Text("Save Attendance")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EnterScoreDialog(
    student: Student,
    onDismiss: () -> Unit,
    onSubmit: (Double) -> Unit
) {
    var scoreText by remember { mutableStateOf("75") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Enter Assessment Score",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter recent quiz/test percentage for ${student.name}:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = scoreText,
                    onValueChange = {
                        scoreText = it
                        isError = it.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) !in 0.0..100.0
                    },
                    label = { Text("Score Percentage (0-100)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError,
                    supportingText = {
                        if (isError) Text("Please enter a valid percentage between 0 and 100")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("score_input_field")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = scoreText.toDoubleOrNull() ?: 75.0
                    onSubmit(score.coerceIn(0.0, 100.0))
                },
                enabled = !isError && scoreText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                modifier = Modifier.testTag("submit_score_button")
            ) {
                Text("Save Score")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UpdateFeeDialog(
    student: Student,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf("Paid") }
    val statuses = listOf("Paid", "Pending", "Delayed")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Update Fee Payment Status",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Update fee status for ${student.name}:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                statuses.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedStatus == status),
                                onClick = { selectedStatus = status }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedStatus == status),
                            onClick = { selectedStatus = status }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = status, fontSize = 14.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedStatus) },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                modifier = Modifier.testTag("submit_fee_button")
            ) {
                Text("Save Fee Status")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
