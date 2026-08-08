package com.example.model

import java.util.Date

enum class EarlyWarningLevel(
    val displayName: String,
    val urduText: String,
    val minScore: Int,
    val maxScore: Int
) {
    ON_TRACK("On Track", "طریق پر", 0, 29),
    ATTENTION("Needs Attention", "توجہ درکار", 30, 49),
    ELEVATED("Elevated Concern", "بڑھی ہوئی تشویش", 50, 69),
    EARLY_WARNING("Early Warning", "ابتدائی تنبیہ", 70, 100)
}

enum class TrendDirection {
    IMPROVING,
    STABLE,
    DECLINING,
    SIGNIFICANT_DECLINE
}

data class Student(
    val id: String,
    val name: String,
    val className: String = "8-A",
    val section: String = "A",
    val age: Int = 13,
    val guardianName: String,
    val guardianContact: String,
    val enrollmentStatus: String = "Active",
    val avatarUrl: String? = null
)

data class AttendanceRecord(
    val id: String,
    val studentId: String,
    val date: String, // YYYY-MM-DD or week label
    val status: String // Present, Absent, Late
)

data class AssessmentRecord(
    val id: String,
    val studentId: String,
    val subject: String,
    val assessmentName: String,
    val score: Double,
    val total: Double = 100.0,
    val percentage: Double = (score / total) * 100.0,
    val date: String
)

data class FeeRecord(
    val id: String,
    val studentId: String,
    val dueDate: String,
    val paymentDate: String?,
    val amount: Double,
    val status: String // Paid, Pending, Delayed
)
