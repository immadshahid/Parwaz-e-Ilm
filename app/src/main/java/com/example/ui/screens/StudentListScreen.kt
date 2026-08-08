package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudentState
import com.example.model.EarlyWarningLevel
import com.example.ui.components.RiskBadge
import com.example.ui.components.TrendIndicator
import com.example.ui.theme.*

@Composable
fun StudentListScreen(
    studentsState: List<StudentState>,
    onSelectStudent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<EarlyWarningLevel?>(null) }

    val filteredStudents = studentsState.filter { state ->
        val matchesSearch = state.student.name.contains(searchQuery, ignoreCase = true) ||
                state.student.guardianName.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == null || state.riskAssessment.level == selectedFilter
        matchesSearch && matchesFilter
    }.sortedByDescending { it.riskAssessment.score }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmWhite)
            .padding(16.dp)
    ) {
        // Search TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by student or guardian name...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Slate500
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("student_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("All (${studentsState.size})", fontSize = 12.sp) }
                )
            }
            items(EarlyWarningLevel.values()) { level ->
                val count = studentsState.count { it.riskAssessment.level == level }
                FilterChip(
                    selected = selectedFilter == level,
                    onClick = { selectedFilter = if (selectedFilter == level) null else level },
                    label = { Text("${level.displayName} ($count)", fontSize = 12.sp) },
                    modifier = Modifier.testTag("filter_chip_${level.name}")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CLASS PULSE • ${filteredStudents.size} STUDENTS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Slate500,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredStudents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No students found matching your criteria.",
                    color = Slate500,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStudents, key = { it.student.id }) { state ->
                    StudentListCard(
                        studentState = state,
                        onClick = { onSelectStudent(state.student.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentListCard(
    studentState: StudentState,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("student_list_item_${studentState.student.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Emerald100,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = studentState.student.name.take(1),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Emerald900
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = studentState.student.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Guardian: ${studentState.student.guardianName}",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${studentState.riskAssessment.score}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (studentState.riskAssessment.level) {
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RiskBadge(level = studentState.riskAssessment.level)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Att: ${studentState.riskAssessment.attendanceTrend.currentValue}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )
                    Text(
                        text = "Aca: ${studentState.riskAssessment.academicTrend.currentValue}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )
                }
            }
        }
    }
}
