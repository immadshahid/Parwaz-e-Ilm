package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DemoRepository
import com.example.data.StudentState
import com.example.model.InterventionType
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.Emerald800
import com.example.ui.theme.ParwaazTheme
import com.example.ui.theme.Slate200

sealed class Screen {
    object RoleSelection : Screen()
    object TeacherDashboard : Screen()
    object StudentList : Screen()
    data class StudentProfile(val studentId: String) : Screen()
    data class AIReasoning(val studentId: String) : Screen()
    data class Intervention(val studentId: String) : Screen()
    data class ImpactTracking(val studentId: String) : Screen()
    object AdminDashboard : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParwaazTheme {
                ParwaazApp()
            }
        }
    }
}

@Composable
fun ParwaazApp() {
    val studentsState by DemoRepository.studentsState.collectAsState()
    val interventions by DemoRepository.interventions.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.TeacherDashboard) }
    var currentRole by remember { mutableStateOf("Teacher") } // "Teacher" or "Admin"

    // Dialog state for quick data logging
    var attendanceDialogTarget by remember { mutableStateOf<StudentState?>(null) }
    var scoreDialogTarget by remember { mutableStateOf<StudentState?>(null) }
    var feeDialogTarget by remember { mutableStateOf<StudentState?>(null) }

    Scaffold(
        topBar = {
            if (currentScreen !is Screen.RoleSelection) {
                HeaderBar(
                    currentRole = currentRole,
                    onRoleToggle = {
                        if (currentRole == "Teacher") {
                            currentRole = "Admin"
                            currentScreen = Screen.AdminDashboard
                        } else {
                            currentRole = "Teacher"
                            currentScreen = Screen.TeacherDashboard
                        }
                    },
                    onResetDemo = {
                        DemoRepository.resetDemoData()
                        currentScreen = Screen.TeacherDashboard
                    }
                )
            }
        },
        bottomBar = {
            if (currentScreen !is Screen.RoleSelection) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    NavigationBarItem(
                        selected = currentScreen is Screen.TeacherDashboard || currentScreen is Screen.AdminDashboard,
                        onClick = {
                            currentScreen = if (currentRole == "Teacher") Screen.TeacherDashboard else Screen.AdminDashboard
                        },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text("Dashboard", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_dashboard")
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.StudentList,
                        onClick = { currentScreen = Screen.StudentList },
                        icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = "Class Pulse") },
                        label = { Text("Class Pulse", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_class_pulse")
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.StudentProfile && (currentScreen as Screen.StudentProfile).studentId == "STU-001",
                        onClick = { currentScreen = Screen.StudentProfile("STU-001") },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Amina Khan") },
                        label = { Text("Amina Khan", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_amina_profile")
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.RoleSelection,
                        onClick = { currentScreen = Screen.RoleSelection },
                        icon = { Icon(Icons.Default.SwapHoriz, contentDescription = "Roles") },
                        label = { Text("Role", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_roles")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val screen = currentScreen) {
                is Screen.RoleSelection -> {
                    RoleSelectionScreen(
                        onSelectTeacher = {
                            currentRole = "Teacher"
                            currentScreen = Screen.TeacherDashboard
                        },
                        onSelectAdmin = {
                            currentRole = "Admin"
                            currentScreen = Screen.AdminDashboard
                        },
                        onLaunchAminaDemo = {
                            currentRole = "Teacher"
                            currentScreen = Screen.StudentProfile("STU-001")
                        }
                    )
                }
                is Screen.TeacherDashboard -> {
                    TeacherDashboardScreen(
                        studentsState = studentsState,
                        onSelectStudent = { id -> currentScreen = Screen.StudentProfile(id) },
                        onNavigateToClassPulse = { currentScreen = Screen.StudentList },
                        onOpenAttendanceDialog = { state -> attendanceDialogTarget = state },
                        onOpenScoreDialog = { state -> scoreDialogTarget = state },
                        onOpenFeeDialog = { state -> feeDialogTarget = state }
                    )
                }
                is Screen.StudentList -> {
                    StudentListScreen(
                        studentsState = studentsState,
                        onSelectStudent = { id -> currentScreen = Screen.StudentProfile(id) }
                    )
                }
                is Screen.StudentProfile -> {
                    val state = studentsState.find { it.student.id == screen.studentId } ?: studentsState.first()
                    StudentProfileScreen(
                        studentState = state,
                        onViewAIReasoning = { currentScreen = Screen.AIReasoning(state.student.id) },
                        onCreateIntervention = { currentScreen = Screen.Intervention(state.student.id) },
                        onViewImpact = { currentScreen = Screen.ImpactTracking(state.student.id) },
                        onOpenAttendanceDialog = { attendanceDialogTarget = state },
                        onOpenScoreDialog = { scoreDialogTarget = state },
                        onOpenFeeDialog = { feeDialogTarget = state },
                        onBack = { currentScreen = Screen.TeacherDashboard }
                    )
                }
                is Screen.AIReasoning -> {
                    val state = studentsState.find { it.student.id == screen.studentId } ?: studentsState.first()
                    AIReasoningScreen(
                        studentState = state,
                        onCreateIntervention = { currentScreen = Screen.Intervention(state.student.id) },
                        onBack = { currentScreen = Screen.StudentProfile(state.student.id) }
                    )
                }
                is Screen.Intervention -> {
                    val state = studentsState.find { it.student.id == screen.studentId } ?: studentsState.first()
                    InterventionScreen(
                        studentState = state,
                        onSaveIntervention = { type, date, assigned, notes, english, urdu ->
                            DemoRepository.createIntervention(state.student.id, type, date, assigned, notes, english, urdu)
                            currentScreen = Screen.StudentProfile(state.student.id)
                        },
                        onSimulateOutcome = {
                            DemoRepository.simulateInterventionOutcome(state.student.id)
                            currentScreen = Screen.ImpactTracking(state.student.id)
                        },
                        onBack = { currentScreen = Screen.StudentProfile(state.student.id) }
                    )
                }
                is Screen.ImpactTracking -> {
                    val state = studentsState.find { it.student.id == screen.studentId } ?: studentsState.first()
                    ImpactTrackingScreen(
                        studentState = state,
                        onBack = { currentScreen = Screen.StudentProfile(state.student.id) }
                    )
                }
                is Screen.AdminDashboard -> {
                    AdminDashboardScreen(
                        studentsState = studentsState,
                        interventions = interventions,
                        onSelectStudent = { id -> currentScreen = Screen.StudentProfile(id) }
                    )
                }
            }
        }

        // Data Logging Dialogs
        attendanceDialogTarget?.let { state ->
            RecordAttendanceDialog(
                student = state.student,
                onDismiss = { attendanceDialogTarget = null },
                onSubmit = { status ->
                    DemoRepository.recordAttendance(state.student.id, status)
                    attendanceDialogTarget = null
                }
            )
        }

        scoreDialogTarget?.let { state ->
            EnterScoreDialog(
                student = state.student,
                onDismiss = { scoreDialogTarget = null },
                onSubmit = { score ->
                    DemoRepository.enterAssessmentScore(state.student.id, score)
                    scoreDialogTarget = null
                }
            )
        }

        feeDialogTarget?.let { state ->
            UpdateFeeDialog(
                student = state.student,
                onDismiss = { feeDialogTarget = null },
                onSubmit = { status ->
                    DemoRepository.updateFeeStatus(state.student.id, status)
                    feeDialogTarget = null
                }
            )
        }
    }
}
