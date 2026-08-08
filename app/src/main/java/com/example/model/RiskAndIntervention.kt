package com.example.model

data class SignalTrend(
    val currentValue: String,
    val previousValue: String,
    val changePercentage: Double,
    val trend: TrendDirection,
    val description: String,
    val historyValues: List<Double>
)

data class ContributingSignal(
    val signalName: String, // e.g. "Attendance", "Academic Performance", "Fee Payment"
    val scoreImpact: Int,   // points contributed to risk
    val statusText: String, // e.g. "Declined by 24% over 5 weeks"
    val trend: TrendDirection
)

data class RiskAssessment(
    val studentId: String,
    val score: Int, // 0 to 100
    val level: EarlyWarningLevel,
    val attendanceTrend: SignalTrend,
    val academicTrend: SignalTrend,
    val feeTrend: SignalTrend,
    val crossSignalDeterioration: Boolean,
    val contributingSignals: List<ContributingSignal>,
    val evidenceList: List<String>,
    val generatedAt: String,
    val trendSummary: String
)

enum class InterventionType(val title: String, val iconName: String) {
    TEACHER_FOLLOWUP("Teacher One-on-One", "person"),
    COUNSELOR_REFERRAL("Counselor Session", "support_agent"),
    PARENT_CONTACT("Parent Call / Meeting", "family_restroom"),
    SCHEDULE_MEETING("Multi-disciplinary Meeting", "groups")
}

enum class InterventionStatus(val label: String) {
    PLANNED("Planned"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    RESOLVED("Support Resolved")
}

data class Intervention(
    val id: String,
    val studentId: String,
    val type: InterventionType,
    val status: InterventionStatus,
    val createdAt: String,
    val targetDate: String,
    val assignedTo: String,
    val notes: String,
    val parentMessageDraftEnglish: String = "",
    val parentMessageDraftUrdu: String = ""
)

data class InterventionOutcome(
    val interventionId: String,
    val studentId: String,
    val dateCompleted: String,
    val attendanceBefore: Double,
    val attendanceAfter: Double,
    val performanceBefore: Double,
    val performanceAfter: Double,
    val feeStatusBefore: String,
    val feeStatusAfter: String,
    val riskBefore: Int,
    val riskAfter: Int,
    val narrative: String
)
